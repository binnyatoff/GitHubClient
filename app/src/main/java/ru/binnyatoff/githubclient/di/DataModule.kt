package ru.binnyatoff.githubclient.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.binnyatoff.githubclient.repository.Repository
import ru.binnyatoff.githubclient.data.network.retrofit.Api
import ru.binnyatoff.githubclient.data.database.Dao
import ru.binnyatoff.githubclient.data.database.GitDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideGitDatabase(@ApplicationContext context: Context): GitDatabase {
        return Room.databaseBuilder(
            context,
            GitDatabase::class.java,
            "data"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(gitDatabase: GitDatabase): Dao {
        return gitDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api, dao: Dao): Repository {
        return Repository(api, dao)
    }
}