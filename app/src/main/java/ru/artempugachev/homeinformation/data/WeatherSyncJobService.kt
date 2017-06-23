package ru.artempugachev.homeinformation.data

import android.os.AsyncTask
import com.firebase.jobdispatcher.*


/**
 * Job service for weather sync
 */
class WeatherSyncJobService : JobService() {
    lateinit var fetchWeatherTask: FetchWeatherTask

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        fetchWeatherTask = FetchWeatherTask()
        fetchWeatherTask.execute(jobParameters)
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        fetchWeatherTask.cancel(true)
        return true
    }

    inner class FetchWeatherTask : AsyncTask<JobParameters, Unit, Unit>() {
        lateinit var jobParameters: JobParameters
        override fun doInBackground(vararg params: JobParameters) {
            jobParameters = params[0]
            syncWeather(this@WeatherSyncJobService)
        }

        override fun onPostExecute(result: Unit?) {
            jobFinished(jobParameters, false)
        }
    }

}