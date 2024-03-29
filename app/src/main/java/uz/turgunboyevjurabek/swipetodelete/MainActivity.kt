@file:OptIn(ExperimentalMaterial3Api::class)

package uz.turgunboyevjurabek.swipetodelete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.turgunboyevjurabek.swipetodelete.ui.theme.SwipeToDeleteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeToDeleteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val programmingLanguage= remember {
                        mutableStateListOf(
                            "Kotlin",
                            "Java",
                            "C++",
                            "Python",
                            "C#",
                            "C",
                            "JavaScript",
                            "Go",
                            "Php",
                            "Swift",
                            "Dart"
                        ) }

                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                    ){
                        items(items = programmingLanguage,
                            key = {it}
                        ){language->
                            SwipeToDeleteCointainer(item =language,
                                onDelete ={
                                    programmingLanguage-=language
                                }) {
                                Text(text = language, modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(16.dp),
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun <T> SwipeToDeleteCointainer(
    item:T,
    onDelete:(T)->Unit,
    animationDuration:Int=1000,
    content:@Composable (T) ->Unit
) {

    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state= rememberDismissState(
        confirmValueChange = {value->
            if (value == DismissValue.DismissedToStart){
                isRemoved=true
                true
            }else{
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved){
        if (isRemoved){
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(visible = !isRemoved, exit = shrinkVertically(
        animationSpec = tween(durationMillis = animationDuration),
        shrinkTowards = Alignment.Top) + fadeOut()
    ) {
        SwipeToDismiss(state = state,
            background = { DeleteBacround(swipeDismissState = state)},
            dismissContent ={content(item)},
            directions = setOf(DismissDirection.EndToStart)
        )

    }

}


@Composable
fun DeleteBacround(
    swipeDismissState: DismissState,
) {
    val color=if(swipeDismissState.dismissDirection == DismissDirection.EndToStart)
        Color.Red
    else Color.Transparent

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color)
        .padding(16.dp),
        contentAlignment = Alignment.CenterEnd) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.White)
    }

}