/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.invoice.api.user;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import com.google.inject.Inject;
import com.ning.billing.ErrorCode;
import com.ning.billing.account.api.Account;
import com.ning.billing.account.api.AccountApiException;
import com.ning.billing.account.api.AccountUserApi;
import com.ning.billing.catalog.api.Currency;
import com.ning.billing.invoice.InvoiceDispatcher;
import com.ning.billing.invoice.api.Invoice;
import com.ning.billing.invoice.api.InvoiceApiException;
import com.ning.billing.invoice.api.InvoiceItem;
import com.ning.billing.invoice.api.InvoicePayment;
import com.ning.billing.invoice.api.InvoiceUserApi;
import com.ning.billing.invoice.dao.InvoiceDao;
import com.ning.billing.invoice.template.HtmlInvoiceGenerator;
import com.ning.billing.util.api.TagApiException;
import com.ning.billing.util.callcontext.CallContext;

public class DefaultInvoiceUserApi implements InvoiceUserApi {
    private final InvoiceDao dao;
    private final InvoiceDispatcher dispatcher;
    private final AccountUserApi accountUserApi;
    private final HtmlInvoiceGenerator generator;

    @Inject
    public DefaultInvoiceUserApi(final InvoiceDao dao, final InvoiceDispatcher dispatcher, final AccountUserApi accountUserApi, final HtmlInvoiceGenerator generator) {
        this.dao = dao;
        this.dispatcher = dispatcher;
        this.accountUserApi = accountUserApi;
        this.generator = generator;
    }

    @Override
    public List<Invoice> getInvoicesByAccount(final UUID accountId) {
        return dao.getInvoicesByAccount(accountId);
    }

    @Override
    public List<Invoice> getInvoicesByAccount(final UUID accountId, final DateTime fromDate) {
        return dao.getInvoicesByAccount(accountId, fromDate);
    }

    @Override
    public void notifyOfPaymentAttempt(final InvoicePayment invoicePayment, final CallContext context) {
        dao.notifyOfPaymentAttempt(invoicePayment, context);
    }

    @Override
    public BigDecimal getAccountBalance(final UUID accountId) {
        final BigDecimal result = dao.getAccountBalance(accountId);
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public Invoice getInvoice(final UUID invoiceId) {
        return dao.getById(invoiceId);
    }

    @Override
    public List<Invoice> getUnpaidInvoicesByAccountId(final UUID accountId, final DateTime upToDate) {
        return dao.getUnpaidInvoicesByAccountId(accountId, upToDate);
    }

    @Override
    public Invoice triggerInvoiceGeneration(final UUID accountId,
                                            final DateTime targetDate, final boolean dryRun,
                                            final CallContext context) throws InvoiceApiException {
        Invoice result = dispatcher.processAccount(accountId, targetDate, dryRun, context);
        if (result == null) {
            throw new InvoiceApiException(ErrorCode.INVOICE_NOTHING_TO_DO, accountId, targetDate);
        } else {
            return result;
        }
    }

    @Override
    public void tagInvoiceAsWrittenOff(final UUID invoiceId, final CallContext context) throws TagApiException {
        dao.setWrittenOff(invoiceId, context);
    }

    @Override
    public void tagInvoiceAsNotWrittenOff(final UUID invoiceId, final CallContext context) throws TagApiException {
        dao.removeWrittenOff(invoiceId, context);
    }

    @Override
    public InvoiceItem getCreditById(final UUID creditId) throws InvoiceApiException {
        return dao.getCreditById(creditId);
    }

    @Override
    public InvoiceItem insertCredit(final UUID accountId, final BigDecimal amount, final DateTime effectiveDate,
                                    final Currency currency, final CallContext context) throws InvoiceApiException {
        return dao.insertCredit(accountId, null, amount, effectiveDate, currency, context);
    }

    @Override
    public InvoiceItem insertCreditForInvoice(UUID accountId, UUID invoiceId,
            BigDecimal amount, DateTime effectiveDate, Currency currency,
            CallContext context) throws InvoiceApiException {
        return dao.insertCredit(accountId, invoiceId, amount, effectiveDate, currency, context);
    }


    @Override
    public String getInvoiceAsHTML(final UUID invoiceId) throws AccountApiException, IOException, InvoiceApiException {
        final Invoice invoice = getInvoice(invoiceId);
        if (invoice == null) {
            throw new InvoiceApiException(ErrorCode.INVOICE_NOT_FOUND, invoiceId);
        }

        final Account account = accountUserApi.getAccountById(invoice.getAccountId());
        return generator.generateInvoice(account, invoice);
    }
}
