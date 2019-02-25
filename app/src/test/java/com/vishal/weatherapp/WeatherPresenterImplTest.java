package com.vishal.weatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import com.vishal.weatherapp.pojo.Current;
import com.vishal.weatherapp.pojo.Error;
import com.vishal.weatherapp.pojo.Location;
import com.vishal.weatherapp.pojo.TemperatureResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;

/**
 * Test class for {@link WeatherPresenterImpl}. {@link PowerMockito} is used for mocking the
 * static methods.
 *
 * @author Vishal - 25th Feb 2019
 * @since 1.0.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringUtils.class, DateFormat.class})
@PowerMockIgnore("javax.net.ssl.*")
public class WeatherPresenterImplTest {
    private WeatherContract.Model model;
    @Mock
    private WeatherContract.View view;
    private WeatherContract.Presenter presenter;
    private TestScheduler testScheduler;
    private Observable<TemperatureResponse> newObservable;
    @Mock
    private Context context;
    @Mock
    private Resources resources;
    @Mock
    private TemperatureResponse temperatureResponse;
    @Mock
    private Error error;
    @Mock
    private Location location;
    @Mock
    private Current current;
    private String errorText = "Something went wrong";

    /**
     * Basic setup is done here, like creating supported objects or intercepting ny values of a
     * method call.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(StringUtils.class);
        PowerMockito.mockStatic(DateFormat.class);
        testScheduler = new TestScheduler();
        model = new WeatherModelImpl(context);
        presenter = new WeatherPresenterImpl(view, model, testScheduler, testScheduler);
        PowerMockito.when(StringUtils.isEmpty(any(CharSequence.class))).thenAnswer(
                new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        CharSequence a = (CharSequence) invocation.getArguments()[0];
                        return !(a != null && a.length() > 0);
                    }
                });
    }

    @Test
    public void initView() {
        presenter.initView();
        Mockito.verify(view).onInitView();
        Mockito.verify(view).handleWeatherView(false);
        Mockito.verify(view).handleErrorView(false);
    }

    @Test
    public void getWeatherData() {
        PowerMockito.when(DateFormat.format(any(CharSequence.class), any(Calendar.class))).
                thenReturn("25 Feb 2019");
        presenter.getWeatherData("");
        Mockito.verify(view).showErrorMessage(model.getInvalidCityMessage());
        TemperatureResponse inputData = new TemperatureResponse();
        newObservable = Observable.just(inputData);
        Mockito.when(context.getResources()).thenReturn(resources);

        presenter.getWeatherData("Delhi");

        testScheduler.triggerActions();
        TestObserver testObserver = TestObserver.create();
        newObservable.subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertSubscribed();
        testObserver.assertComplete();

        //API call test
        TestObserver<TemperatureResponse> responseTestObserver = TestObserver.create();
        model.initiateWeatherInfoCall("Patna").subscribe(responseTestObserver);
        responseTestObserver.assertNoErrors();
        responseTestObserver.assertSubscribed();
        responseTestObserver.assertComplete();
        assertThat(responseTestObserver.values().get(0).getLocation().getName(), is("Patna"));
        assertThat(responseTestObserver.values().get(0).getLocation().getRegion(), is("Bihar"));
        assertThat(responseTestObserver.values().get(0).getLocation().getCountry(), is("India"));

    }

    @Test
    public void handleTemperatureResponse() {
        presenter.handleTemperatureResponse(null);
        Mockito.verify(view).handleErrorView(true);

        Mockito.when(temperatureResponse.getLocation()).thenReturn(location);
        Mockito.when(temperatureResponse.getCurrent()).thenReturn(current);
        presenter.handleTemperatureResponse(temperatureResponse);
        Mockito.verify(view).handleLoaderView(false);

        Mockito.when(temperatureResponse.getError()).thenReturn(error);
        Mockito.when(error.getMessage()).thenReturn(errorText);
        presenter.handleTemperatureResponse(temperatureResponse);
        Mockito.verify(view).showErrorMessage(errorText);
        Mockito.verify(view).handleWeatherView(true);

    }
}