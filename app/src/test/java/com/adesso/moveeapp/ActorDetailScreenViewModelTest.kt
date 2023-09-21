package com.adesso.moveeapp

import com.adesso.moveeapp.data.model.actormoviecreditsmodel.ActorMovieCreditsModel
import com.adesso.moveeapp.data.model.actormoviecreditsmodel.CastMovie
import com.adesso.moveeapp.data.model.actortvcreditsmodel.ActorTvCreditsModel
import com.adesso.moveeapp.data.model.actortvcreditsmodel.CastTv
import com.adesso.moveeapp.data.model.persondetailsmodel.PersonDetailsModel
import com.adesso.moveeapp.data.repository.PersonRepository
import com.adesso.moveeapp.ui.home.actordetailscreen.ActorDetailScreenViewModel
import com.adesso.moveeapp.ui.home.actordetailscreen.model.ActorCreditUiModel
import com.adesso.moveeapp.ui.home.actordetailscreen.model.ActorUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.state.DataState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorDetailScreenViewModelTest {

    private val mockPersonRepository = mockk<PersonRepository>()

    private lateinit var viewModel: ActorDetailScreenViewModel
    private val actorId = 1234

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ActorDetailScreenViewModel(
            mockPersonRepository
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getActorDetail returns success`() = runBlocking {
        val person = mockk<PersonDetailsModel>()
        every { person.id } returns 1234
        every { person.adult } returns false
        every { person.biography } returns "bioghraphy"
        every { person.name } returns "Irina Rimes"
        every { person.birthday } returns "1997-07-07"
        every { person.alsoKnownAs } returns emptyList()
        every { person.deathDay } returns ""
        every { person.knownForDepartment } returns ""
        every { person.placeOfBirth } returns "Romania"
        every { person.popularity } returns 100.0
        every { person.profilePath } returns ""
        every { person.gender } returns 1
        every { person.homepage } returns ""
        every { person.imdbId } returns "1234"

        val expectedValue = ActorUiModel(
            image = person.profilePath ?: "",
            name = person.name ?: "",
            biography = person.biography ?: "",
            born = DataTransformer.concatBornStatus(
                birthDate = person.birthday ?: "",
                placeOfBirth = person.placeOfBirth ?: ""
            )
        )
        coEvery { mockPersonRepository.getPersonDetail(actorId) } returns ApiResponse.Success(person)
        viewModel.getActorDetail(actorId)

        assert(viewModel.actorDetailFlow.value == DataState.Success(expectedValue))
    }

    @Test
    fun `getActorDetail returns error`() = runBlocking {
        coEvery { mockPersonRepository.getPersonDetail(actorId) } returns ApiResponse.Error(
            "An Error Occurred!"
        )
        viewModel.getActorDetail(actorId)
        assert(viewModel.actorDetailFlow.value is DataState.Error)

    }

    @Test
    fun `getTvCredits returns success`() = runBlocking {
        val castTv1 = CastTv(
            adult = false,
            backdropPath = "/path/to/backdrop",
            character = "Character Name",
            creditId = "credit123",
            episodeCount = 10,
            firstAirDate = "2023-01-01",
            genreIds = listOf(1, 2, 3),
            id = 1,
            name = "Show Name",
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Original Show Name",
            overview = "This is an overview of the show.",
            popularity = 8.5,
            posterPath = "/path/to/poster",
            voteAverage = 7.5,
            voteCount = 200
        )

        val castTv2 = CastTv(
            adult = false,
            backdropPath = "/path/to/backdrop2",
            character = "Character Name 2",
            creditId = "credit456",
            episodeCount = 20,
            firstAirDate = "2023-02-01",
            genreIds = listOf(4, 5, 6),
            id = 2,
            name = "Show Name 2",
            originCountry = listOf("UK"),
            originalLanguage = "en",
            originalName = "Original Show Name 2",
            overview = "This is an overview of the second show.",
            popularity = 9.0,
            posterPath = "/path/to/poster2",
            voteAverage = 8.0,
            voteCount = 300
        )

        val tvCreditApiResponseData = ActorTvCreditsModel(
            id = actorId,
            castTv = listOf(castTv1, castTv2)
        )

        val expectedData =
            tvCreditApiResponseData.castTv.map { cast ->
                ActorCreditUiModel(
                imagePath = cast.posterPath,
                name = cast.name,
                voteAverage = DataTransformer.roundToNearest(cast.voteAverage),
                date = DataTransformer.transformSpecialDateFormat(cast.firstAirDate),
                id = cast.id
                )
            }


        coEvery {
            mockPersonRepository.getPersonTvCredits(personId = actorId)
        } returns ApiResponse.Success(tvCreditApiResponseData)

        viewModel.getTvCredits(actorId)

        assert(viewModel.tvCreditsFlow.value == DataState.Success(expectedData))
    }

    @Test
    fun `getTvCredits returns error`() = runBlocking {
        coEvery {
            mockPersonRepository.getPersonTvCredits(personId = actorId)
        } returns ApiResponse.Error("An error occurred!")

        viewModel.getTvCredits(actorId)

        assert(viewModel.tvCreditsFlow.value is DataState.Error)
    }

    @Test
    fun `getMovieCredits returns success`() = runBlocking {
        val castMovie1 = CastMovie(
            adult = false,
            backdropPath = "/path/to/backdrop1",
            character = "Character Name 1",
            creditId = "credit123",
            genreIds = listOf(1, 2, 3),
            id = 1,
            order = 1,
            originalLanguage = "en",
            originalTitle = "Original Title 1",
            overview = "This is an overview of the first movie.",
            popularity = 8.5,
            posterPath = "/path/to/poster1",
            releaseDate = "2023-01-01",
            title = "Movie Title 1",
            video = false,
            voteAverage = 7.5,
            voteCount = 200
        )

        val castMovie2 = CastMovie(
            adult = false,
            backdropPath = "/path/to/backdrop2",
            character = "Character Name 2",
            creditId = "credit456",
            genreIds = listOf(4, 5, 6),
            id = 2,
            order = 2,
            originalLanguage = "en",
            originalTitle = "Original Title 2",
            overview = "This is an overview of the second movie.",
            popularity = 9.0,
            posterPath = "/path/to/poster2",
            releaseDate = "2023-02-01",
            title = "Movie Title 2",
            video = false,
            voteAverage = 8.0,
            voteCount = 300
        )


        val movieCreditApiResponse = ActorMovieCreditsModel(
            id = actorId,
            castMovie = listOf(castMovie1, castMovie2),

        )

        val expectedData =
            movieCreditApiResponse.castMovie.map {cast ->
                ActorCreditUiModel(
                    imagePath = cast.posterPath,
                    name = cast.title,
                    voteAverage = DataTransformer.roundToNearest(cast.voteAverage),
                    date = DataTransformer.transformSpecialDateFormat(cast.releaseDate),
                    id = cast.id
                )
            }

        coEvery {
            mockPersonRepository.getPersonMovieCredits(actorId)
        } returns ApiResponse.Success(movieCreditApiResponse)

        viewModel.getMovieCredits(actorId)

        assert(viewModel.movieCreditsFlow.value == DataState.Success(expectedData))
    }

    @Test
    fun `getMovieCredits returns error`() = runBlocking {
        coEvery {
            mockPersonRepository.getPersonMovieCredits(actorId)
        } returns ApiResponse.Error("An Error Occurred!")

        viewModel.getMovieCredits(actorId)

        assert(viewModel.movieCreditsFlow.value is DataState.Error)
    }
}