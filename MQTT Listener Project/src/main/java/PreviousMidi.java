import java.time.LocalDateTime;

/**
 * Created by Koen on 21/12/2016.
 */
public class PreviousMidi {
    private int channel;
    private int control;
    private LocalDateTime localDateTime;

    public PreviousMidi(int channel, int control, LocalDateTime localDateTime) {
        this.channel = channel;
        this.control = control;
        this.localDateTime = localDateTime;
    }

    public int getChannel() {
        return channel;
    }

    public int getControl() {
        return control;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreviousMidi that = (PreviousMidi) o;

        if (channel != that.channel) return false;
        return control == that.control;
    }

    @Override
    public int hashCode() {
        int result = channel;
        result = 31 * result + control;
        return result;
    }
}
