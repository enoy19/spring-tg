package io.enoy.tg.action;

public enum MessageValidationValue {
    VALID, NOT_VALID, NOT_EXISTING;

    public static MessageValidationValue valueFor(boolean value) {
        return value ? VALID : NOT_VALID;
    }

    public MessageValidationValue chainValidity(MessageValidationValue other) {

        switch (this) {
            case VALID:
                if (other.isValidOrNotExisting())
                    return VALID;
            case NOT_VALID:
                return NOT_VALID;
            case NOT_EXISTING:
                if (other.isNotExisting())
                    return NOT_EXISTING;
                else if (other.isValid())
                    return VALID;
                else
                    return NOT_VALID;
        }

        return NOT_VALID;
    }

    public boolean isValidOrNotExisting() {
        return isValid() || isNotExisting();
    }

    public boolean isValid() {
        return this.equals(VALID);
    }

    public boolean isNotExisting() {
        return this.equals(NOT_EXISTING);
    }
}
