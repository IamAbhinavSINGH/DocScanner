package com.example.docscanner.feature_createpdf.presentation.createpdf.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.docscanner.R
import com.example.docscanner.feature_createpdf.domain.util.OrderType
import com.example.docscanner.feature_createpdf.domain.util.PdfOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePdfToolbar(
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit,
    onOrderClicked: () -> Unit,
    onOrderChanged: (PdfOrder) -> Unit,
    isOrderVisible: Boolean,
    onNavigationDrawerClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    pdfOrder: PdfOrder
){
    Column(
        modifier = modifier
    ){

        val orderIcon = if(isOrderVisible) Icons.AutoMirrored.Filled.Sort else Icons.AutoMirrored.Outlined.Sort

        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                scrolledContainerColor = MaterialTheme.colorScheme.primary
            ),
            title = {
                Text(
                    "Documents",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigationDrawerClicked() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "navigation drawer",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { onSearchClicked() },
                    colors = IconButtonDefaults.iconButtonColors(
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { onOrderClicked() }) {
                    Icon(
                        imageVector = orderIcon,
                        contentDescription = "Sort"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )

        val isOrderSectionVisible = if(scrollBehavior.state.collapsedFraction > 0.6f) false else isOrderVisible

        AnimatedVisibility(
            visible = isOrderSectionVisible
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                onOrderChange = {pdfOrder->
                    onOrderChanged(pdfOrder)
                },
                pdfOrder = pdfOrder
            )
        }
    }
}

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    pdfOrder: PdfOrder = PdfOrder.Date(OrderType.Descending),
    onOrderChange: (PdfOrder) -> Unit
){
    Column(
        modifier = modifier
    ){
        Row{
            DefaultRadioButton(
                title = "Title",
                isSelected = pdfOrder is PdfOrder.Title,
                onSelect = {onOrderChange(PdfOrder.Title(pdfOrder.orderType))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Description",
                isSelected = pdfOrder is PdfOrder.Description,
                onSelect = {onOrderChange(PdfOrder.Description(pdfOrder.orderType))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Date",
                isSelected = pdfOrder is PdfOrder.Date,
                onSelect = {onOrderChange(PdfOrder.Date(pdfOrder.orderType))}
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row{
            DefaultRadioButton(
                title = "Ascending",
                isSelected = pdfOrder.orderType is OrderType.Ascending,
                onSelect = {onOrderChange(pdfOrder.copy(orderType = OrderType.Ascending))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Descending",
                isSelected = pdfOrder.orderType is OrderType.Descending,
                onSelect = {onOrderChange(pdfOrder.copy(orderType = OrderType.Descending))}
            )
        }
    }
}


@Composable
fun DefaultRadioButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit
){
    Row(
        modifier = modifier.clickable {
            onSelect()
        },
        verticalAlignment = Alignment.CenterVertically
    ){
        RadioButton(
            selected = isSelected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(
                unselectedColor = MaterialTheme.colorScheme.primary,
                selectedColor = MaterialTheme.colorScheme.surface
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

