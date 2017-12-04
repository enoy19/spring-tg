package io.enoy.tg.action;

public enum ValidType {
    VALID, NOT_VALID, NOT_EXISTING;

    public static ValidType fromBoolean(boolean value) {
        return value ? VALID : NOT_VALID;
    }

    /**
     * chains one {@link ValidType} with another.
     * <table>
     * <tr>
     * <th>Type A</th>
     * <th>Type B</th>
     * <th>Type RESULT</th>
     * </tr>
     * <tr>
     * <td>VALID</td>
     * <td>VALID</td>
     * <td>VALID</td>
     * </tr>
     * <tr>
     * <td>VALID</td>
     * <td>INVALID</td>
     * <td>INVALID</td>
     * </tr>
     * <tr>
     * <td>INVALID</td>
     * <td>INVALID</td>
     * <td>INVALID</td>
     * </tr>
     * <tr>
     * <td>VALID</td>
     * <td>NOT_EXISTING</td>
     * <td>VALID</td>
     * </tr>
     * <tr>
     * <td>NOT_EXISTING</td>
     * <td>NOT_EXISTING</td>
     * <td>NOT_EXISTING</td>
     * </tr>
     * </table>
     *
     * @param other {@link ValidType} to chain
     * @return ValidType that results of both
     */
    public ValidType chainType(ValidType other) {
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
