package com.example.shoppinglist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

data class shoppingListItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isediting: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyShoppingList() {
    val context = LocalContext.current

    var sitems by remember {
        mutableStateOf(listOf<shoppingListItem>())
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }
    var showDialog by remember {
        mutableStateOf(
            false
        )
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        Button(
            onClick = {
                showDialog = true

            },
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )                        .padding(top = 16.dp)
        ) {
            Text(text = "Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {

            items(sitems) { item ->
                if (item.isediting) {
                    shoppingItemEditor(
                        item = item,
                        onEditComplete = { itemName, itemQuantity ->
                            sitems =
                                sitems.map { it.copy(isediting = false) }
                            val editeditem=sitems.find { it.id==item.id }
                            editeditem?.let { it.name=itemName
                            it.quantity=itemQuantity}
                        })
                } else {
                    ShoppingList(
                        item =item,
                        onEditclick = {
                            sitems=sitems.map { it.copy(isediting = it.id==item.id) }
                        }, onDeleteclick = {
                            sitems=sitems-item
                        })
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            10.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Button(
                        onClick = {
                            if (itemName.isNotBlank() && itemQuantity.isNotBlank() && itemQuantity.isDigitsOnly()) {
                                var newShoppingItem =
                                    shoppingListItem(
                                        id = sitems.size + 1,
                                        name = itemName,
                                        quantity = itemQuantity.toInt(),
                                    )
                                sitems = sitems + newShoppingItem
//                                Toast.makeText(context,"${itemQuantity} ${itemName} added ",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                itemName = ""
                                itemQuantity = ""

                            }
                            else if (itemName.isBlank()){
                                Toast.makeText(context,"Enter valid Name",Toast.LENGTH_SHORT).show()
                            }
                            else if (itemQuantity.isBlank() || !itemQuantity.isDigitsOnly()){
                                Toast.makeText(context,"Enter valid Quantity",Toast.LENGTH_SHORT).show()
                            }
                        }) {
                        Text(
                            text = "Add"
                        )
                    }
                    Button(
                        onClick = {
                            showDialog =
                                false
                        }) {
                        Text(
                            text = "Cancel"
                        )
                    }
                }
            },
            title = {
                Text(
                    text = "Add Shopping Item"
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {
                            itemName =
                                it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {
                            itemQuantity =
                                it
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            })
    }
}


@Composable
fun ShoppingList(
    item: shoppingListItem,
    onEditclick: () -> Unit,
    onDeleteclick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                border = BorderStroke(
                    2.dp,
                    Color(0xFF008080)
                ),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceAround

    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(
            text = "Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp)
        )
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditclick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
            IconButton(onClick = onDeleteclick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }

}
