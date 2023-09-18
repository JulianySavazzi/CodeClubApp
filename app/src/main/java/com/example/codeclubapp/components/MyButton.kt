package com.example.codeclubapp.components


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codeclubapp.ui.theme.WHITE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


//botao
@Composable
fun MyButton(
    //variaveis que serao passadas para o botao
    text: String,
    route: String,
    navController: NavController,
    modifier: Modifier,
    onValueChange: (String) -> Unit
    //state: MutableState<Boolean>
) {
    Button(onClick = {
        //passar variavel com a rota para mudar de tela
        goRoutesNoAuth(route, navController)
        //navController.navigate(route = route)
        //modifier
        onValueChange
    }) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            color = WHITE,
            fontSize = 18.sp
            )
        modifier
        /*
        Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .height(56.dp) */

    }
}

@Composable
fun MyLoginButton(
    text: String,
    //route: String,
    //navController: NavController,
    modifier: Modifier,
    //onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    //isLoginGoogle: Boolean
){

    Button(onClick) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            color = WHITE,
            fontSize = 18.sp
        )
    }
}

//*********************** VERIFY AUTH ***********************
fun verifyLogin() :Boolean{
    if (Firebase.auth.currentUser != null) {
       return true
    } else return false
}

fun goRoutesNoAuth(route: String, navController: NavController){
    //verificar se usuario tem permissao para acessar a pagina antes de chamar o navigate
    var loged = verifyLogin()
    if(route == "login" || route == "poll" ||
        route == "user_teacher" || route == "user_student"
        || route == "login_teacher"){
        navController.navigate(route = route)
    } else {
        if(loged == true){
            ckeckRoutes(route, navController)
        }
    }

}
fun ckeckRoutes(route: String, navController: NavController):Boolean{
    if(route == "feed" || route == "notifications" ||
        route == "feed_teacher" || route == "notifications_teacher" ||
        route == "teacher" || route == "manageStudents" ||
        route == "manageProjects" || route == "manageTeams" ||
        route == "managePolls" || route == "manageFeed" ||
        route == "student"
        && Firebase.auth.currentUser != null) {
        navController.navigate(route = route)
        return true
    }
    else return false
}