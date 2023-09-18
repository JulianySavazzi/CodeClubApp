package com.example.codeclubapp.components

import android.widget.Toast
import android.widget.Toast.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codeclubapp.datasource.DataSource
import com.example.codeclubapp.repository.TeacherRepository
import com.example.codeclubapp.ui.theme.WHITE
import com.example.codeclubapp.view.Teacher
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import okhttp3.internal.notify

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
        } else {
            //emitir alerta dizendo que nao tem permissao para acessar a pagina
            //Toast.makeText("você não pode acessar essa paágina",1,LENGTH_SHORT).show()
        }
    }

}
fun ckeckRoutes(route: String, navController: NavController):Boolean{
    //val db = FirebaseFirestore.getInstance()
    //var teacher = db.collection("teacher").id.toString()
    //var presentUser = Firebase.auth.currentUser!!.uid.toString()
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
    /*
    if (route == "teacher" || route == "manageStudents" ||
        route == "manageProjects" || route == "manageTeams" ||
        route == "managePolls" || route == "manageFeed"
        && teacher == presentUser) {
        //verificar se o user é professor
        navController.navigate(route = route)
        println("tentando ir para a rota -> $presentUser = $teacher" )
        return true
    } else return false
    if (route == "student" /*&& db.collection("student").id ==
        Firebase.auth.currentUser!!.uid*/) {
        //verificar se o user é aluno
        navController.navigate(route = route)
        return true
    } else return false
     */
}