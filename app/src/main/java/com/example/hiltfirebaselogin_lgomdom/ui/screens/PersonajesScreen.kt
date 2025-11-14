// File: `app/src/main/java/com/example/hiltfirebaselogin_lgomdom/ui/screens/PersonajesScreen.kt`
package com.example.hiltfirebaselogin_lgomdom.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hiltfirebaselogin_lgomdom.models.Habilidad
import com.example.hiltfirebaselogin_lgomdom.models.Personaje
import com.example.hiltfirebaselogin_lgomdom.ui.viewmodel.PersonajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonajesScreen(
    navController: NavController,
    viewModel: PersonajeViewModel = hiltViewModel()
) {
    val personajes by viewModel.personajes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Personaje?>(null) }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    var deleteTargetId by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personajes") },
                actions = {
                    Button(onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("personajes") { inclusive = true }
                        }
                    }) {
                        Text("Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Personaje")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(personajes) { personaje ->
                        PersonajeItem(
                            personaje = personaje,
                            onEdit = {
                                editTarget = personaje
                                showEditDialog = true
                            },
                            onDelete = {
                                deleteTargetId = personaje.id
                                showDeleteConfirm = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Create dialog
            if (showCreateDialog) {
                PersonajeDialog(
                    title = "Crear Personaje",
                    initialNombre = "",
                    initialTripulacion = "",
                    initialHabilidades = emptyList(),
                    onConfirm = { nombre, tripulacion, habilidades ->
                        viewModel.createPersonaje(
                            Personaje(
                                nombre = nombre,
                                tripulacion = tripulacion,
                                habilidades = habilidades
                            )
                        )
                        showCreateDialog = false
                    },
                    onDismiss = { showCreateDialog = false }
                )
            }

            // Edit dialog
            if (showEditDialog && editTarget != null) {
                PersonajeDialog(
                    title = "Editar Personaje",
                    initialNombre = editTarget!!.nombre,
                    initialTripulacion = editTarget!!.tripulacion,
                    initialHabilidades = editTarget!!.habilidades,
                    onConfirm = { nombre, tripulacion, habilidades ->
                        val id = editTarget!!.id
                        if (id != null) {
                            viewModel.updatePersonaje(
                                id,
                                Personaje(
                                    id = id,
                                    nombre = nombre,
                                    tripulacion = tripulacion,
                                    habilidades = habilidades
                                )
                            )
                        }
                        showEditDialog = false
                        editTarget = null
                    },
                    onDismiss = {
                        showEditDialog = false
                        editTarget = null
                    }
                )
            }

            // Delete confirmation
            if (showDeleteConfirm && deleteTargetId != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false; deleteTargetId = null },
                    title = { Text("Confirmar borrado") },
                    text = { Text("¿Seguro que quieres borrar este personaje? Esta acción no se puede deshacer.") },
                    confirmButton = {
                        TextButton(onClick = {
                            deleteTargetId?.let { viewModel.deletePersonaje(it) }
                            showDeleteConfirm = false
                            deleteTargetId = null
                        }) {
                            Text("Borrar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirm = false; deleteTargetId = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PersonajeDialog(
    title: String,
    initialNombre: String,
    initialTripulacion: String,
    initialHabilidades: List<Habilidad>,
    onConfirm: (String, String, List<Habilidad>) -> Unit,
    onDismiss: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf(initialNombre) }
    var tripulacion by rememberSaveable { mutableStateOf(initialTripulacion) }

    // Estado dinámico para entradas de habilidades: lista de pares (nombre, descripcion)
    val habilidadesState = remember {
        mutableStateListOf<Pair<String, String>>().apply {
            if (initialHabilidades.isNotEmpty()) {
                initialHabilidades.forEach { add(it.nombre to it.descripcion) }
            } else {
                add("" to "")
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tripulacion,
                    onValueChange = { tripulacion = it },
                    label = { Text("Tripulación") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Habilidades", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                habilidadesState.forEachIndexed { index, pair ->
                    val (hNombre, hDesc) = pair
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = hNombre,
                            onValueChange = { new -> habilidadesState[index] = new to pair.second },
                            label = { Text("Nombre habilidad") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = hDesc,
                            onValueChange = { new -> habilidadesState[index] = pair.first to new },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                if (habilidadesState.size > 1) habilidadesState.removeAt(index)
                                else habilidadesState[index] = "" to ""
                            }) {
                                Text("Eliminar habilidad")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                TextButton(onClick = { habilidadesState.add("" to "") }) {
                    Text("Añadir habilidad")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isBlank() || tripulacion.isBlank()) return@TextButton
                val habilidades = habilidadesState.mapNotNull {
                    val (hn, hd) = it
                    if (hn.isBlank() && hd.isBlank()) null else Habilidad(nombre = hn.trim(), descripcion = hd.trim())
                }
                onConfirm(nombre.trim(), tripulacion.trim(), habilidades)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PersonajeItem(
    personaje: Personaje,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = personaje.nombre, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tripulación: ${personaje.tripulacion}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (personaje.habilidades.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Habilidades:", style = MaterialTheme.typography.bodySmall)
                    personaje.habilidades.forEach { h ->
                        Text(text = "- ${h.nombre}: ${h.descripcion}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar")
                }
            }
        }
    }
}
