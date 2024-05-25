package com.example.docscanner.feature_createpdf.presentation.searchpdf.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.docscanner.feature_createpdf.domain.Pdf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarPdfs(
    query: String,
    onValueChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onCloseSearch: () -> Unit,
    onBackPressed: () -> Unit,
    onPdfClick: (Pdf) -> Unit,
    pdfList: List<Pdf>
) {

    val color = MaterialTheme.colorScheme.surface
    val generalColor = MaterialTheme.colorScheme.primary
    val dividerColor =
        if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary

    val modifier =
        if (isActive)
            Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(
                    RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
        else
            Modifier
                .fillMaxWidth()
                .padding(16.dp)

    SearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = {
            onValueChange(it)
        },
        onSearch = {
            onSearchClicked(it)
        },
        active = isActive,
        onActiveChange = {
            onActiveChange(it)
        },
        placeholder = {
            Text(
                text = " Search here!!",
                color = generalColor,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 15.sp
            )
        },
        leadingIcon = {
            if (!isActive) {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "back",
                        tint = generalColor
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint = generalColor
                )
            }
        },
        trailingIcon = {
            if (isActive) {
                Icon(
                    modifier = Modifier.clickable {
                        onCloseSearch()
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove query"
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = color,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = generalColor,
                cursorColor = generalColor,
                unfocusedTextColor = generalColor,
                focusedTrailingIconColor = generalColor,
                unfocusedIndicatorColor = generalColor,
            ),
            dividerColor = dividerColor
        ),
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 8.dp
    ) {
        if (pdfList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    text = "Couldn't find anything!!",
                    color = generalColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(pdfList) { pdf ->
                    SearchResults(
                        modifier = Modifier.fillMaxWidth(),
                        pdfItem = pdf,
                        queryParam = query,
                        onClick = {
                            onPdfClick(pdf)
                        }
                    )
                }
            }
        }
    }
}
