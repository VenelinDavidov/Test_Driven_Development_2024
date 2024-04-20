package TirePressureMonitoringSystemTest06;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import p06_TirePressureMonitoringSystem.Alarm;
import p06_TirePressureMonitoringSystem.Sensor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMonitoringSystemTirePressure06 {

    Alarm alarm;
    Sensor sensor;

    @Before
    public void setUp() {
        sensor = mock(Sensor.class);
        alarm = new Alarm(sensor);
    }

    @Test
    public void testAlarmShouldBeOnBecauseLowPressure() {
        Sensor sensor = Mockito.mock(Sensor.class);
        when(sensor.popNextPressurePsiValue()).thenReturn(14.0);
        alarm.check();
        Assert.assertTrue(alarm.getAlarmOn());
    }

    @Test
    public void testAlarmShouldBeOnBecauseHightPressure() {
        Sensor sensor = Mockito.mock(Sensor.class);
        when(sensor.popNextPressurePsiValue()).thenReturn(25.0);
        alarm.check();
        Assert.assertTrue(alarm.getAlarmOn());
    }

    @Test
    public void testAlarmShouldBeOff() {
        when(sensor.popNextPressurePsiValue()).thenReturn(20.0);
        alarm.check();
        Assert.assertFalse(alarm.getAlarmOn());
    }

}
