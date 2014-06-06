/*
 * Copyright 2014 Groupon, Inc
 *
 * Groupon licenses this file to you under the Apache License, version 2.0
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

package org.killbill.billing.payment.dao;

import java.math.BigDecimal;
import java.util.UUID;

import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.entity.EntityBase;
import org.killbill.billing.payment.api.DirectPaymentTransaction;
import org.killbill.billing.payment.api.PaymentStatus;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.util.dao.TableName;
import org.killbill.billing.util.entity.dao.EntityModelDao;

import com.google.common.base.Objects;

public class DirectPaymentTransactionModelDao extends EntityBase implements EntityModelDao<DirectPaymentTransaction> {

    private UUID directPaymentId;
    private String externalKey;
    private TransactionType transactionType;
    private DateTime effectiveDate;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;
    private Currency currency;
    private BigDecimal processedAmount;
    private Currency processedCurrency;
    private String gatewayErrorCode;
    private String gatewayErrorMsg;

    public DirectPaymentTransactionModelDao() { /* For the DAO mapper */ }

    public DirectPaymentTransactionModelDao(final UUID id, @Nullable final String externalKey, @Nullable final DateTime createdDate, @Nullable final DateTime updatedDate,
                                            final UUID directPaymentId, final TransactionType transactionType, final DateTime effectiveDate,
                                            final PaymentStatus paymentStatus, final BigDecimal amount, final Currency currency, final String gatewayErrorCode, final String gatewayErrorMsg) {
        super(id, createdDate, updatedDate);
        this.externalKey = Objects.firstNonNull(externalKey, id.toString());
        this.directPaymentId = directPaymentId;
        this.transactionType = transactionType;
        this.effectiveDate = effectiveDate;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.currency = currency;
        this.processedAmount = null;
        this.processedCurrency = null;
        this.gatewayErrorCode = gatewayErrorCode;
        this.gatewayErrorMsg = gatewayErrorMsg;
    }

    public DirectPaymentTransactionModelDao(@Nullable final DateTime createdDate, @Nullable final DateTime updatedDate,
                                            @Nullable final String externalKey, final UUID directPaymentId, final TransactionType transactionType, final DateTime effectiveDate,
                                            final PaymentStatus paymentStatus, final BigDecimal amount, final Currency currency, final String gatewayErrorCode, final String gatewayErrorMsg) {
        this(UUID.randomUUID(), externalKey, createdDate, updatedDate, directPaymentId, transactionType, effectiveDate, paymentStatus, amount, currency, gatewayErrorCode, gatewayErrorMsg);
    }

    public UUID getDirectPaymentId() {
        return directPaymentId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getProcessedAmount() {
        return processedAmount;
    }

    public Currency getProcessedCurrency() {
        return processedCurrency;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }

    public String getGatewayErrorMsg() {
        return gatewayErrorMsg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DirectPaymentTransactionModelDao{");
        sb.append("directPaymentId=").append(directPaymentId);
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", transactionType=").append(transactionType);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", paymentStatus=").append(paymentStatus);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", processedAmount=").append(processedAmount);
        sb.append(", processedCurrency=").append(processedCurrency);
        sb.append(", gatewayErrorCode='").append(gatewayErrorCode).append('\'');
        sb.append(", gatewayErrorMsg='").append(gatewayErrorMsg).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final DirectPaymentTransactionModelDao that = (DirectPaymentTransactionModelDao) o;

        if (amount != null ? amount.compareTo(that.amount) != 0 : that.amount != null) {
            return false;
        }
        if (currency != that.currency) {
            return false;
        }
        if (directPaymentId != null ? !directPaymentId.equals(that.directPaymentId) : that.directPaymentId != null) {
            return false;
        }
        if (effectiveDate != null ? effectiveDate.compareTo(that.effectiveDate) != 0 : that.effectiveDate != null) {
            return false;
        }
        if (externalKey != null ? !externalKey.equals(that.externalKey) : that.externalKey != null) {
            return false;
        }
        if (gatewayErrorCode != null ? !gatewayErrorCode.equals(that.gatewayErrorCode) : that.gatewayErrorCode != null) {
            return false;
        }
        if (gatewayErrorMsg != null ? !gatewayErrorMsg.equals(that.gatewayErrorMsg) : that.gatewayErrorMsg != null) {
            return false;
        }
        if (paymentStatus != that.paymentStatus) {
            return false;
        }
        if (processedAmount != null ? processedAmount.compareTo(that.processedAmount) != 0 : that.processedAmount != null) {
            return false;
        }
        if (processedCurrency != that.processedCurrency) {
            return false;
        }
        if (transactionType != that.transactionType) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (directPaymentId != null ? directPaymentId.hashCode() : 0);
        result = 31 * result + (externalKey != null ? externalKey.hashCode() : 0);
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (paymentStatus != null ? paymentStatus.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (processedAmount != null ? processedAmount.hashCode() : 0);
        result = 31 * result + (processedCurrency != null ? processedCurrency.hashCode() : 0);
        result = 31 * result + (gatewayErrorCode != null ? gatewayErrorCode.hashCode() : 0);
        result = 31 * result + (gatewayErrorMsg != null ? gatewayErrorMsg.hashCode() : 0);
        return result;
    }

    @Override
    public TableName getTableName() {
        return TableName.DIRECT_TRANSACTIONS;
    }

    @Override
    public TableName getHistoryTableName() {
        return TableName.DIRECT_TRANSACTION_HISTORY;
    }
}