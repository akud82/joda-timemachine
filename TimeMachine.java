import com.akudsoft.timemachine;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.joda.time.DateTimeUtils.*;

/**
 * Example:
 * <p><blockquote><pre>
 * TimeMachine.in(LocalDate.now().plusDays(1), (today) -> {
 *     eventService.publish(DailySubscriptionWithdrawalEvent.of(customer));
 *     assertThat(subscription.getEndedAt(), is(equalTo(LocalDate.now().plusDays(6))));
 * });
 *  </pre></blockquote>
 */
public class TimeMachine {
    public static void in(LocalDate date, Consumer<LocalDate> method) throws Exception {
        final MillisProvider provider = getCurrentTimeMillisProvider();
        DateTimeUtils.setCurrentMillisFixed(date.toDate().getTime());
        try {
            method.accept(date);
        } finally {
            DateTimeUtils.setCurrentMillisProvider(provider);
        }
    }

    private static MillisProvider getCurrentTimeMillisProvider() throws Exception {
        final Field providerField = DateTimeUtils.class.getDeclaredField("cMillisProvider");
        providerField.setAccessible(true);
        return (MillisProvider) providerField.get(null);
    }
}

