package com.example.codeclubapp.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Icon
import android.util.AttributeSet
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.codeclubapp.R
import com.example.codeclubapp.components.MyAppBarBottom
import com.example.codeclubapp.components.MyAppBarTop
import com.example.codeclubapp.model.Feed
import com.example.codeclubapp.repository.FeedRepository
import com.example.codeclubapp.ui.theme.RedCode
import com.example.codeclubapp.ui.theme.WHITE
import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.wait

@Composable
fun Feed(navController: NavController){
    //apenas ler as publicações

    val loginTeacher = remember {
        mutableStateOf(false)
    }

    val loginStudent = remember {
        mutableStateOf(true)
    }

    val feedRepository = FeedRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "feed de notícias")
        //Rows -> corpo do app
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "últimas notícias:",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold

            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(10.dp)
            //.background(MaterialTheme.colorScheme.tertiary)
        ){
            //corpo da publicação
            //var publicationsList: MutableList<Feed> = mutableListOf()
            val publicationsList: MutableList<Feed> = feedRepository.getFeed().collectAsState(
                //se o estado da lista for vazio vai retornar uma mutableListOf
                //se a lista tiver preenchida vai retornar os valores dos documentos
                mutableListOf()
            ).value

            //preencher lista

            //componente de listagem
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                itemsIndexed(publicationsList){
                        position, _ -> MyListFeed(position = position, listItem = publicationsList)
                }
            }

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
        }
    }
}

//mostrar lista de publicações
@Composable
fun MyListFeed(
    position: Int,
    listItem: MutableList<Feed>
){
    val context: Context = LocalContext.current

    //ligar a view com a model
    val titleFeed = listItem[position].name
    val descriptionFeed = listItem[position].description

    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(15.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(15.dp)
        ) {
            // Create references for the composables to constrain
            val(
                txtTitle,
                txtDescription
                //navBarItemEdit,
                //navBarItemDelete
            ) = createRefs()

            Text(
                text = titleFeed.toString(),
                modifier = Modifier.constrainAs(txtTitle) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = descriptionFeed.toString(),
                modifier = Modifier.constrainAs(txtDescription) {
                    top.linkTo(txtTitle.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}

//******************************************************************************** THEACHER ********************************************************************************

//Feed do professor permite que ele edite ou exclua a publicação que ele selecionar
@Composable
fun FeedTeacher(navController: NavController){

    val context: Context = LocalContext.current

    val loginTeacher = remember {
        mutableStateOf(true)
    }

    val loginStudent = remember {
        mutableStateOf(false)
    }

    val feedRepository = FeedRepository()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.background)
    ){
        MyAppBarTop(title = "feed de notícias")
        //Rows -> corpo do app
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = "últimas notícias:",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold

            )
        }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.90f)
                    .padding(10.dp)
                //.background(MaterialTheme.colorScheme.tertiary)
            ){
                //corpo da publicação
                //var publicationsList: MutableList<Feed> = mutableListOf()
                val publicationsList: MutableList<Feed> = feedRepository.getFeed().collectAsState(
                    //se o estado da lista for vazio vai retornar uma mutableListOf
                    //se a lista tiver preenchida vai retornar os valores dos documentos
                    mutableListOf()
                ).value

                //preencher lista

                //componente de listagem
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    itemsIndexed(publicationsList){
                            position, _ -> MyListFeedTeacher(position = position, listItem = publicationsList, context = context, navController = navController)
                    }
                }

            }
         Row(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(),
             horizontalArrangement = Arrangement.Center,
             verticalAlignment = Alignment.Bottom
         ){
             MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
         }

        //MyAppBarBottom(navController = navController, loginStudent = loginStudent, loginTeacher = loginTeacher)
    }
}


//mostrar lista de publicações e permitir editar e excluir
@Composable
fun MyListFeedTeacher(
    position: Int,
    listItem: MutableList<Feed>,
    context: Context,
    navController: NavController
){
    //val context: Context = LocalContext.current

    //ligar a view com a model
    val titleFeed = listItem[position].name
    val descriptionFeed = listItem[position].description

    val scope = rememberCoroutineScope()

    val repository = FeedRepository()

    fun deleteDialog(){
        //deletar publicação
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("EXCLUIR PUBLICAÇÃO")
            .setMessage("tem certeza que quer excluir essa publicação?")
            .setPositiveButton("Sim"){
                _, _, ->

                repository.deleteFeed(titleFeed.toString(), descriptionFeed.toString())

                scope.launch(Dispatchers.Main){
                    //remover publicacao excluida da lista
                    listItem.removeAt(position)
                    //navegar para a pagina feed para atualizar a listagem
                    navController.navigate("feed_teacher")
                    Toast.makeText(context, "publicação excluída com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Não"){
                _, _, ->
            }
            .show()
    }

    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
    Card(
        modifier = Modifier.padding(15.dp
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(15.dp)
        ) {
            // Create references for the composables to constrain
            val(
                txtTitle,
                txtDescription,
                navBarItemEdit,
                navBarItemDelete
            ) = createRefs()

            Text(
                text = titleFeed.toString(),
                modifier = Modifier.constrainAs(txtTitle) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            Text(
                text = descriptionFeed.toString(),
                modifier = Modifier.constrainAs(txtDescription) {
                    top.linkTo(txtTitle.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                }
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.constrainAs(navBarItemEdit) {
                    top.linkTo(txtDescription.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 15.dp)
                },
            ) {
                //Text(text = "editar"),
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_edit_24), contentDescription ="editar")
            }

            IconButton(
                onClick = {
                    //deletar publicacao
                          deleteDialog()
                          },
                modifier = Modifier.constrainAs(navBarItemDelete) {
                    top.linkTo(txtDescription.bottom, margin = 15.dp)
                    start.linkTo(navBarItemEdit.end, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
            ) {
                //Text(text = "excluir")
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.icon_delete_24), contentDescription ="excluir")
            }
        }
    }
    Divider(
        thickness = 15.dp,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    )
}