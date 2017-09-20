package io.qameta.allure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author charlie (Dmitry Baev).
 */
@Data
@Accessors(chain = true)

public class Statistic implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long failed;
    protected long broken;
    protected long skipped;
    protected long knownissuesonly;
    protected long pending;
    protected long passed;
    protected long notcovered;
    protected long unknown;

    @JsonProperty
    public long getTotal() {
        return getFailed() + getBroken() + getPassed() + getKnownissuesonly() + getSkipped() + getPending()
                + getNotcovered() + getUnknown();
    }

    /**
     * To ignore total property during deserialization. Should not be used manually.
     *
     * @deprecated Do not use it manually.
     */
    @Deprecated
    @JsonProperty
    public void setTotal(final long total) {
        //do nothing
    }

    public long get(final Status status) {
        switch (status) {
            case FAILED:
                return getFailed();
            case BROKEN:
                return getBroken();
            case PASSED:
                return getPassed();
            case KNOWN_ISSUES_ONLY:
                return getKnownissuesonly();
            case SKIPPED:
                return getSkipped();
            case PENDING:
                return getPending();
            case NOT_COVERED:
                return getNotcovered();
            default:
                return getUnknown();
        }
    }

    @JsonIgnore
    public Status getStatus() {
        for (final Status status : Status.values()) {
            if (get(status) > 0) {
                return status;
            }
        }
        return Status.UNKNOWN;
    }

    public void update(final Statusable statusable) {
        if (Objects.isNull(statusable)) {
            return;
        }
        update(statusable.getStatus());
    }

    public void update(final Status status) {
        if (Objects.isNull(status)) {
            return;
        }
        switch (status) {
            case FAILED:
                setFailed(getFailed() + 1);
                break;
            case BROKEN:
                setBroken(getBroken() + 1);
                break;
            case PASSED:
                setPassed(getPassed() + 1);
                break;
            case KNOWN_ISSUES_ONLY:
                setKnownissuesonly(getKnownissuesonly() + 1);
                break;
            case SKIPPED:
                setSkipped(getSkipped() + 1);
                break;
            case PENDING:
                setPending(getPending() + 1);
                break;
            case NOT_COVERED:
                setNotcovered(getNotcovered() + 1);
                break;
            default:
                setUnknown(getUnknown() + 1);
                break;
        }
    }

    public void merge(final Statistic other) {
        if (Objects.isNull(other)) {
            return;
        }
        setFailed(getFailed() + other.getFailed());
        setBroken(getBroken() + other.getBroken());
        setPassed(getPassed() + other.getPassed());
        setKnownissuesonly(getKnownissuesonly() + other.getKnownissuesonly());
        setSkipped(getSkipped() + other.getSkipped());
        setPending(getPending() + other.getPending());
        setNotcovered(getNotcovered() + other.getNotcovered());
        setUnknown(getUnknown() + other.getUnknown());
    }

    public static Comparator<Statistic> comparator() {
        return Comparator.comparing(Statistic::getFailed)
                .thenComparing(Statistic::getBroken)
                .thenComparing(Statistic::getPassed)
                .thenComparing(Statistic::getKnownissuesonly)
                .thenComparing(Statistic::getSkipped)
                .thenComparing(Statistic::getPending)
                .thenComparing(Statistic::getNotcovered)
                .thenComparing(Statistic::getUnknown);
    }
}
