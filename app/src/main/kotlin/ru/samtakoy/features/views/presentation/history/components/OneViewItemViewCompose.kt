package ru.samtakoy.features.views.presentation.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.samtakoy.core.app.utils.asAnnotated
import ru.samtakoy.core.presentation.base.getLoremIpsum
import ru.samtakoy.core.presentation.design_system.base.MyColors
import ru.samtakoy.core.presentation.design_system.base.MyOffsets

@Composable
fun OneViewItemView(
    model: OneViewHistoryItemModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = model.themeTitle,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = model.packTitle,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = model.viewDate,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .align(Alignment.Start),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = model.viewedInfo,
            modifier = Modifier
                .wrapContentWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .align(Alignment.End),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = model.errorCount,
            modifier = Modifier
                .wrapContentWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .align(Alignment.End),
            color = MyColors.getErrorTextColor(),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun OneViewItemView_Preview() = MaterialTheme {
    Column(
        modifier = Modifier
            .background(MyColors.getScreenBackground())
            .padding(MyOffsets.small),
        verticalArrangement = Arrangement.spacedBy(MyOffsets.small)
    ) {
        getOneViewItemViewPreviewItems().forEach {
            OneViewItemView(
                model = it,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

internal fun getOneViewItemViewPreviewItems(): ImmutableList<OneViewHistoryItemModel> {
    return persistentListOf(
        OneViewHistoryItemModel(
            id = 1,
            qPackId = 1,
            themeTitle = "Тема такая-то".asAnnotated(),
            packTitle = "Набор карточек".asAnnotated(),
            viewDate = "2021.02.01 10:10".asAnnotated(),
            viewedInfo = "Просмотрено: 3/10".asAnnotated(),
            errorCount = "Ошибок: 2".asAnnotated()
        ),
        OneViewHistoryItemModel(
            id = 2,
            qPackId = 2,
            themeTitle = "..".asAnnotated(),
            packTitle = "resultApi".asAnnotated(),
            viewDate = "2024.02.01 20:20".asAnnotated(),
            viewedInfo = "Просмотрено: 9/10".asAnnotated(),
            errorCount = "Ошибок: -".asAnnotated()
        ),
        OneViewHistoryItemModel(
            id = 1,
            qPackId = 1,
            themeTitle = getLoremIpsum(10).asAnnotated(),
            packTitle = "Набор карточек".asAnnotated(),
            viewDate = "2021.02.01 10:10".asAnnotated(),
            viewedInfo = "Просмотрено: 3/10".asAnnotated(),
            errorCount = "Ошибок: 2".asAnnotated()
        ),
    )
}