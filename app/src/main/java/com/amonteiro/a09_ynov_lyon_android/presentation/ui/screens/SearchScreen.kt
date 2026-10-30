package com.amonteiro.a09_ynov_lyon_android.presentation.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.amonteiro.a09_ynov_lyon_android.R
import com.amonteiro.a09_ynov_lyon_android.data.fake.WeatherFakeDataSource
import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import com.amonteiro.a09_ynov_lyon_android.presentation.MainViewModel
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.MyError
import com.amonteiro.a09_ynov_lyon_android.presentation.ui.theme.AppTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "fr"
)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreProjetTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            val fakelist = WeatherFakeDataSource.loadFakeData()

            SearchScreenContent(
                modifier = Modifier.padding(innerPadding),
                list = fakelist,
                runInProgress = true,
                errorMessage = "Un erreur est survenue"
            )
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "fr"
)
@Composable
fun SearchScreenNoDataPreview() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            SearchScreenContent(
                modifier = Modifier.padding(innerPadding),
                list = emptyList(),
                runInProgress = false,
                errorMessage = ""
            )
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val list = mainViewModel.dataList.collectAsStateWithLifecycle().value
    val runInProgress = mainViewModel.runInProgress.collectAsStateWithLifecycle().value
    val errorMessage = mainViewModel.errorMessage.collectAsStateWithLifecycle().value

    SearchScreenContent(
        modifier = modifier,
        list = list,
        onBtLoadClick = {mainViewModel.loadWeathers(it)},
        runInProgress = runInProgress,
        errorMessage = errorMessage
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    list: List<Weather>,
    onBtLoadClick: (String) -> Unit = {},
    runInProgress: Boolean,
    errorMessage: String
    ) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val searchText: MutableState<String> = rememberSaveable { mutableStateOf("") }

        SearchBar(text = searchText.value, onValueChange = {searchText.value = it})

        MyError(errorMessage = errorMessage)

        AnimatedVisibility (runInProgress) {
            CircularProgressIndicator()
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(list.size) {
                PictureRowItem(
                    data = list[it], modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.onTertiary)
                            .fillMaxWidth()
                )
            }
        }

        Row {

            Button(
                onClick = { searchText.value = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clear filter")
            }

            Button(
                onClick = { onBtLoadClick(searchText.value) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Load data")
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, text : String, onValueChange: (String) -> Unit) {

    TextField(
        value = text, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans strings.xml, R de votre package com.nom.projet
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        //keyboardActions = KeyboardActions(onSearch = {onSearchAction()}), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@Composable
fun PictureRowItem(modifier: Modifier = Modifier, data: Weather) {

    var expended by remember { mutableStateOf(false) }

    Row(modifier = modifier) {

        //Permission Internet nécessaire
        AsyncImage(
            model = data.icon,
            //Pour aller le chercher dans strings.xml R de votre package com.nom.projet
            //contentDescription = stringResource(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(R.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
        )

        Column(modifier = Modifier
            .clickable { expended = !expended }
            .fillMaxWidth()) {
            Text(text = data.name, fontSize = 20.sp)
            Text(
                text = if (expended) data.getResume() else (data.getResume().take(20) + "..."), fontSize = 14.sp,
                modifier = Modifier.animateContentSize()
            )
        }
    }


}