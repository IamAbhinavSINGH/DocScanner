package com.example.docscanner.feature_createpdf.di

import androidx.activity.ComponentActivity
import com.example.docscanner.core.util.FileManager
import com.example.docscanner.feature_createpdf.data.RealmDatabaseRepositoryImpl
import com.example.docscanner.feature_createpdf.domain.Pdf
import com.example.docscanner.feature_createpdf.domain.RealmDatabaseRepo
import com.example.docscanner.feature_createpdf.domain.usecase.PdfUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealmDatabase(): Realm{
        val config = RealmConfiguration.Builder(
            schema = setOf(Pdf::class)
        )
            .schemaVersion(4)
            .build()

        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideRealmDatabaseRepo(realm: Realm): RealmDatabaseRepo{
        return RealmDatabaseRepositoryImpl(realm)
    }

    @Singleton
    @Provides
    fun providesPdfUseCase(repo: RealmDatabaseRepo): PdfUseCase{
        return PdfUseCase(repo)
    }

    @Singleton
    @Provides
    fun providesFileManager(activity: ComponentActivity): FileManager{
        return FileManager(activity)
    }

}