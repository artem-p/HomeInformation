package ru.artempugachev.homeinformation.data;

import android.os.AsyncTask;
import com.firebase.jobdispatcher.*;


/**
 * Job service for weather sync
 */
public final class WeatherSyncJobService extends JobService {
    private FetchWeatherTask fetchWeatherTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        fetchWeatherTask = new FetchWeatherTask();
        fetchWeatherTask.execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        fetchWeatherTask.cancel(true);
        return true;
    }

    final class FetchWeatherTask extends AsyncTask<JobParameters, Void, Void> {
        private JobParameters jobParameters;

        @Override
        protected Void doInBackground(JobParameters... params) {
            jobParameters = params[0];
            WeatherSyncService.syncWeather(WeatherSyncJobService.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            jobFinished(jobParameters, false);
        }
    }

}