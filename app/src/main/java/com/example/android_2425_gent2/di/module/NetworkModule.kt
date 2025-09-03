package com.example.android_2425_gent2.di.module

import com.example.android_2425_gent2.BuildConfig
import com.example.android_2425_gent2.data.network.battery.BatteryApiService
import com.example.android_2425_gent2.data.network.boat.BoatApiService
import com.example.android_2425_gent2.data.network.notification.NotificationApiService
import com.example.android_2425_gent2.data.network.reservation.ReservationApiService
import com.example.android_2425_gent2.data.network.timeslot.TimeSlotApiService
import com.example.android_2425_gent2.data.network.users.UserApiService
import com.example.android_2425_gent2.data.repository.auth.IAuthRepo
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    override fun serialize(
            src: LocalTime?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_TIME))
    }

    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): LocalTime {
        return LocalTime.parse(json?.asString, DateTimeFormatter.ISO_LOCAL_TIME)
    }
}

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(
            src: LocalDate?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json?.asString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}


class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        return LocalDateTime.parse(json?.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}

object NetworkModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    private val gson =
            GsonBuilder()
                    .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
                    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                    .setLenient()
                    .create()

    private fun provideOkHttpClient(authRepo: IAuthRepo): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authRepo))
            .build()
    }

    private fun provideRetrofit(authRepo: IAuthRepo): Retrofit {
        val okHttpClient = provideOkHttpClient(authRepo)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideTimeSlotApiService(authRepo: IAuthRepo): TimeSlotApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(TimeSlotApiService::class.java)
    }

    fun provideReservationApiService(authRepo: IAuthRepo): ReservationApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(ReservationApiService::class.java)
    }

    fun provideNotificationApiService(authRepo: IAuthRepo): NotificationApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(NotificationApiService::class.java)
    }

    fun provideUserApiSerivce(authRepo: IAuthRepo): UserApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(UserApiService::class.java)
    }

    // make a function to provide the battery api service
    fun provideBatteryApiService(authRepo: IAuthRepo): BatteryApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(BatteryApiService::class.java)
    }

    fun provideBoatApiService(authRepo: IAuthRepo): BoatApiService {
        val retrofit = provideRetrofit(authRepo)
        return retrofit.create(BoatApiService::class.java)
    }
}
