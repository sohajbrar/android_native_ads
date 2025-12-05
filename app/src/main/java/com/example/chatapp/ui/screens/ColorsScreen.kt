package com.example.chatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.wds.theme.BaseColors
import com.example.chatapp.wds.theme.WdsColorScheme
import com.example.chatapp.wds.theme.WdsTheme

private data class ColorItem(
    val name: String,
    val color: Color
)

private data class ColorSection(
    val title: String,
    val colors: List<ColorItem>
)

// Constants
private val HORIZONTAL_PADDING = 16.dp
private val VERTICAL_PADDING = 8.dp
private val SECTION_TOP_PADDING = 16.dp
private val COLOR_SWATCH_WIDTH = 80.dp
private val COLOR_SWATCH_HEIGHT = 32.dp
private val COLOR_SWATCH_CORNER_RADIUS = 8.dp

private fun getSemanticColorSections(colors: WdsColorScheme): List<ColorSection> = listOf(
    ColorSection(
        title = "Content",
        colors = listOf(
            ColorItem("colorContentDefault", colors.colorContentDefault),
            ColorItem("colorContentDeemphasized", colors.colorContentDeemphasized),
            ColorItem("colorContentDisabled", colors.colorContentDisabled),
            ColorItem("colorContentInverse", colors.colorContentInverse),
            ColorItem("colorContentOnAccent", colors.colorContentOnAccent),
            ColorItem("colorContentActionDefault", colors.colorContentActionDefault),
            ColorItem("colorContentActionEmphasized", colors.colorContentActionEmphasized),
            ColorItem("colorContentExternalLink", colors.colorContentExternalLink),
            ColorItem("colorContentRead", colors.colorContentRead),
        )
    ),
    ColorSection(
        title = "Surface",
        colors = listOf(
            ColorItem("colorSurfaceDefault", colors.colorSurfaceDefault),
            ColorItem("colorSurfaceElevatedDefault", colors.colorSurfaceElevatedDefault),
            ColorItem("colorSurfaceElevatedEmphasized", colors.colorSurfaceElevatedEmphasized),
            ColorItem("colorSurfaceEmphasized", colors.colorSurfaceEmphasized),
            ColorItem("colorSurfaceHighlight", colors.colorSurfaceHighlight),
            ColorItem("colorSurfaceInverse", colors.colorSurfaceInverse),
            ColorItem("colorSurfacePressed", colors.colorSurfacePressed),
            ColorItem("colorSurfaceNavBar", colors.colorSurfaceNavBar),
        )
    ),
    ColorSection(
        title = "Accent",
        colors = listOf(
            ColorItem("colorAccent", colors.colorAccent),
            ColorItem("colorAccentDeemphasized", colors.colorAccentDeemphasized),
            ColorItem("colorAccentEmphasized", colors.colorAccentEmphasized),
            ColorItem("colorAlwaysBranded", colors.colorAlwaysBranded),
            ColorItem("colorActivityIndicator", colors.colorActivityIndicator),
        )
    ),
    ColorSection(
        title = "Feedback",
        colors = listOf(
            ColorItem("colorPositive", colors.colorPositive),
            ColorItem("colorPositiveDeemphasized", colors.colorPositiveDeemphasized),
            ColorItem("colorNegative", colors.colorNegative),
            ColorItem("colorNegativeDeemphasized", colors.colorNegativeDeemphasized),
            ColorItem("colorNegativeEmphasized", colors.colorNegativeEmphasized),
            ColorItem("colorWarning", colors.colorWarning),
            ColorItem("colorWarningDeemphasized", colors.colorWarningDeemphasized),
        )
    ),
    ColorSection(
        title = "Background",
        colors = listOf(
            ColorItem("colorBackgroundWashPlain", colors.colorBackgroundWashPlain),
            ColorItem("colorBackgroundWashInset", colors.colorBackgroundWashInset),
            ColorItem("colorBackgroundElevatedWashPlain", colors.colorBackgroundElevatedWashPlain),
            ColorItem("colorBackgroundElevatedWashInset", colors.colorBackgroundElevatedWashInset),
            ColorItem("colorBackgroundDimmer", colors.colorBackgroundDimmer),
        )
    ),
    ColorSection(
        title = "Dividers & Outlines",
        colors = listOf(
            ColorItem("colorDivider", colors.colorDivider),
            ColorItem("colorOutlineDefault", colors.colorOutlineDefault),
            ColorItem("colorOutlineDeemphasized", colors.colorOutlineDeemphasized),
            ColorItem("colorOutlineProfilePhoto", colors.colorOutlineProfilePhoto),
        )
    ),
    ColorSection(
        title = "Chat",
        colors = listOf(
            ColorItem("colorBubbleSurfaceOutgoing", colors.colorBubbleSurfaceOutgoing),
            ColorItem("colorBubbleSurfaceIncoming", colors.colorBubbleSurfaceIncoming),
            ColorItem("colorBubbleSurfaceSystem", colors.colorBubbleSurfaceSystem),
            ColorItem("colorBubbleContentDeemphasized", colors.colorBubbleContentDeemphasized),
            ColorItem("colorChatBackgroundWallpaper", colors.colorChatBackgroundWallpaper),
            ColorItem("colorChatForegroundWallpaper", colors.colorChatForegroundWallpaper),
        )
    ),
    ColorSection(
        title = "Other",
        colors = listOf(
            ColorItem("colorAlwaysBlack", colors.colorAlwaysBlack),
            ColorItem("colorAlwaysWhite", colors.colorAlwaysWhite),
            ColorItem("colorActiveListRow", colors.colorActiveListRow),
            ColorItem("colorFilterSurfaceSelected", colors.colorFilterSurfaceSelected),
            ColorItem("colorStatusSeen", colors.colorStatusSeen),
        )
    ),
)

private val baseColorSections = listOf(
    ColorSection(
        title = "Cool Gray",
        colors = listOf(
            ColorItem("wdsCoolGray50", BaseColors.wdsCoolGray50),
            ColorItem("wdsCoolGray75", BaseColors.wdsCoolGray75),
            ColorItem("wdsCoolGray100", BaseColors.wdsCoolGray100),
            ColorItem("wdsCoolGray200", BaseColors.wdsCoolGray200),
            ColorItem("wdsCoolGray300", BaseColors.wdsCoolGray300),
            ColorItem("wdsCoolGray400", BaseColors.wdsCoolGray400),
            ColorItem("wdsCoolGray500", BaseColors.wdsCoolGray500),
            ColorItem("wdsCoolGray600", BaseColors.wdsCoolGray600),
            ColorItem("wdsCoolGray700", BaseColors.wdsCoolGray700),
            ColorItem("wdsCoolGray800", BaseColors.wdsCoolGray800),
            ColorItem("wdsCoolGray900", BaseColors.wdsCoolGray900),
            ColorItem("wdsCoolGray1000", BaseColors.wdsCoolGray1000),
        )
    ),
    ColorSection(
        title = "Warm Gray",
        colors = listOf(
            ColorItem("wdsWarmGray50", BaseColors.wdsWarmGray50),
            ColorItem("wdsWarmGray75", BaseColors.wdsWarmGray75),
            ColorItem("wdsWarmGray100", BaseColors.wdsWarmGray100),
            ColorItem("wdsWarmGray200", BaseColors.wdsWarmGray200),
            ColorItem("wdsWarmGray300", BaseColors.wdsWarmGray300),
            ColorItem("wdsWarmGray400", BaseColors.wdsWarmGray400),
            ColorItem("wdsWarmGray500", BaseColors.wdsWarmGray500),
            ColorItem("wdsWarmGray600", BaseColors.wdsWarmGray600),
            ColorItem("wdsWarmGray700", BaseColors.wdsWarmGray700),
            ColorItem("wdsWarmGray800", BaseColors.wdsWarmGray800),
            ColorItem("wdsWarmGray900", BaseColors.wdsWarmGray900),
            ColorItem("wdsWarmGray1000", BaseColors.wdsWarmGray1000),
        )
    ),
    ColorSection(
        title = "Neutral Gray",
        colors = listOf(
            ColorItem("wdsNeutralGray50", BaseColors.wdsNeutralGray50),
            ColorItem("wdsNeutralGray75", BaseColors.wdsNeutralGray75),
            ColorItem("wdsNeutralGray100", BaseColors.wdsNeutralGray100),
            ColorItem("wdsNeutralGray200", BaseColors.wdsNeutralGray200),
            ColorItem("wdsNeutralGray300", BaseColors.wdsNeutralGray300),
            ColorItem("wdsNeutralGray400", BaseColors.wdsNeutralGray400),
            ColorItem("wdsNeutralGray500", BaseColors.wdsNeutralGray500),
            ColorItem("wdsNeutralGray600", BaseColors.wdsNeutralGray600),
            ColorItem("wdsNeutralGray700", BaseColors.wdsNeutralGray700),
            ColorItem("wdsNeutralGray800", BaseColors.wdsNeutralGray800),
            ColorItem("wdsNeutralGray900", BaseColors.wdsNeutralGray900),
            ColorItem("wdsNeutralGray1000", BaseColors.wdsNeutralGray1000),
        )
    ),
    ColorSection(
        title = "Cobalt",
        colors = listOf(
            ColorItem("wdsCobalt50", BaseColors.wdsCobalt50),
            ColorItem("wdsCobalt75", BaseColors.wdsCobalt75),
            ColorItem("wdsCobalt100", BaseColors.wdsCobalt100),
            ColorItem("wdsCobalt200", BaseColors.wdsCobalt200),
            ColorItem("wdsCobalt300", BaseColors.wdsCobalt300),
            ColorItem("wdsCobalt400", BaseColors.wdsCobalt400),
            ColorItem("wdsCobalt500", BaseColors.wdsCobalt500),
            ColorItem("wdsCobalt600", BaseColors.wdsCobalt600),
            ColorItem("wdsCobalt700", BaseColors.wdsCobalt700),
            ColorItem("wdsCobalt800", BaseColors.wdsCobalt800),
        )
    ),
    ColorSection(
        title = "Sky Blue",
        colors = listOf(
            ColorItem("wdsSkyBlue50", BaseColors.wdsSkyBlue50),
            ColorItem("wdsSkyBlue75", BaseColors.wdsSkyBlue75),
            ColorItem("wdsSkyBlue100", BaseColors.wdsSkyBlue100),
            ColorItem("wdsSkyBlue200", BaseColors.wdsSkyBlue200),
            ColorItem("wdsSkyBlue300", BaseColors.wdsSkyBlue300),
            ColorItem("wdsSkyBlue400", BaseColors.wdsSkyBlue400),
            ColorItem("wdsSkyBlue500", BaseColors.wdsSkyBlue500),
            ColorItem("wdsSkyBlue600", BaseColors.wdsSkyBlue600),
            ColorItem("wdsSkyBlue700", BaseColors.wdsSkyBlue700),
            ColorItem("wdsSkyBlue800", BaseColors.wdsSkyBlue800),
        )
    ),
    ColorSection(
        title = "Green",
        colors = listOf(
            ColorItem("wdsGreen50", BaseColors.wdsGreen50),
            ColorItem("wdsGreen75", BaseColors.wdsGreen75),
            ColorItem("wdsGreen100", BaseColors.wdsGreen100),
            ColorItem("wdsGreen200", BaseColors.wdsGreen200),
            ColorItem("wdsGreen300", BaseColors.wdsGreen300),
            ColorItem("wdsGreen400", BaseColors.wdsGreen400),
            ColorItem("wdsGreen450", BaseColors.wdsGreen450),
            ColorItem("wdsGreen500", BaseColors.wdsGreen500),
            ColorItem("wdsGreen600", BaseColors.wdsGreen600),
            ColorItem("wdsGreen700", BaseColors.wdsGreen700),
            ColorItem("wdsGreen750", BaseColors.wdsGreen750),
            ColorItem("wdsGreen800", BaseColors.wdsGreen800),
        )
    ),
    ColorSection(
        title = "Emerald",
        colors = listOf(
            ColorItem("wdsEmerald50", BaseColors.wdsEmerald50),
            ColorItem("wdsEmerald75", BaseColors.wdsEmerald75),
            ColorItem("wdsEmerald100", BaseColors.wdsEmerald100),
            ColorItem("wdsEmerald200", BaseColors.wdsEmerald200),
            ColorItem("wdsEmerald300", BaseColors.wdsEmerald300),
            ColorItem("wdsEmerald400", BaseColors.wdsEmerald400),
            ColorItem("wdsEmerald500", BaseColors.wdsEmerald500),
            ColorItem("wdsEmerald600", BaseColors.wdsEmerald600),
            ColorItem("wdsEmerald700", BaseColors.wdsEmerald700),
            ColorItem("wdsEmerald800", BaseColors.wdsEmerald800),
        )
    ),
    ColorSection(
        title = "Teal",
        colors = listOf(
            ColorItem("wdsTeal50", BaseColors.wdsTeal50),
            ColorItem("wdsTeal75", BaseColors.wdsTeal75),
            ColorItem("wdsTeal100", BaseColors.wdsTeal100),
            ColorItem("wdsTeal200", BaseColors.wdsTeal200),
            ColorItem("wdsTeal300", BaseColors.wdsTeal300),
            ColorItem("wdsTeal400", BaseColors.wdsTeal400),
            ColorItem("wdsTeal500", BaseColors.wdsTeal500),
            ColorItem("wdsTeal600", BaseColors.wdsTeal600),
            ColorItem("wdsTeal700", BaseColors.wdsTeal700),
            ColorItem("wdsTeal800", BaseColors.wdsTeal800),
        )
    ),
    ColorSection(
        title = "Yellow",
        colors = listOf(
            ColorItem("wdsYellow50", BaseColors.wdsYellow50),
            ColorItem("wdsYellow75", BaseColors.wdsYellow75),
            ColorItem("wdsYellow100", BaseColors.wdsYellow100),
            ColorItem("wdsYellow200", BaseColors.wdsYellow200),
            ColorItem("wdsYellow300", BaseColors.wdsYellow300),
            ColorItem("wdsYellow400", BaseColors.wdsYellow400),
            ColorItem("wdsYellow500", BaseColors.wdsYellow500),
            ColorItem("wdsYellow600", BaseColors.wdsYellow600),
            ColorItem("wdsYellow700", BaseColors.wdsYellow700),
            ColorItem("wdsYellow800", BaseColors.wdsYellow800),
        )
    ),
    ColorSection(
        title = "Orange",
        colors = listOf(
            ColorItem("wdsOrange50", BaseColors.wdsOrange50),
            ColorItem("wdsOrange75", BaseColors.wdsOrange75),
            ColorItem("wdsOrange100", BaseColors.wdsOrange100),
            ColorItem("wdsOrange200", BaseColors.wdsOrange200),
            ColorItem("wdsOrange300", BaseColors.wdsOrange300),
            ColorItem("wdsOrange400", BaseColors.wdsOrange400),
            ColorItem("wdsOrange500", BaseColors.wdsOrange500),
            ColorItem("wdsOrange600", BaseColors.wdsOrange600),
            ColorItem("wdsOrange700", BaseColors.wdsOrange700),
            ColorItem("wdsOrange800", BaseColors.wdsOrange800),
        )
    ),
    ColorSection(
        title = "Red",
        colors = listOf(
            ColorItem("wdsRed50", BaseColors.wdsRed50),
            ColorItem("wdsRed75", BaseColors.wdsRed75),
            ColorItem("wdsRed100", BaseColors.wdsRed100),
            ColorItem("wdsRed200", BaseColors.wdsRed200),
            ColorItem("wdsRed250", BaseColors.wdsRed250),
            ColorItem("wdsRed300", BaseColors.wdsRed300),
            ColorItem("wdsRed400", BaseColors.wdsRed400),
            ColorItem("wdsRed450", BaseColors.wdsRed450),
            ColorItem("wdsRed500", BaseColors.wdsRed500),
            ColorItem("wdsRed600", BaseColors.wdsRed600),
            ColorItem("wdsRed700", BaseColors.wdsRed700),
            ColorItem("wdsRed800", BaseColors.wdsRed800),
        )
    ),
    ColorSection(
        title = "Pink",
        colors = listOf(
            ColorItem("wdsPink50", BaseColors.wdsPink50),
            ColorItem("wdsPink75", BaseColors.wdsPink75),
            ColorItem("wdsPink100", BaseColors.wdsPink100),
            ColorItem("wdsPink200", BaseColors.wdsPink200),
            ColorItem("wdsPink300", BaseColors.wdsPink300),
            ColorItem("wdsPink400", BaseColors.wdsPink400),
            ColorItem("wdsPink500", BaseColors.wdsPink500),
            ColorItem("wdsPink600", BaseColors.wdsPink600),
            ColorItem("wdsPink700", BaseColors.wdsPink700),
            ColorItem("wdsPink800", BaseColors.wdsPink800),
        )
    ),
    ColorSection(
        title = "Purple",
        colors = listOf(
            ColorItem("wdsPurple50", BaseColors.wdsPurple50),
            ColorItem("wdsPurple75", BaseColors.wdsPurple75),
            ColorItem("wdsPurple100", BaseColors.wdsPurple100),
            ColorItem("wdsPurple200", BaseColors.wdsPurple200),
            ColorItem("wdsPurple300", BaseColors.wdsPurple300),
            ColorItem("wdsPurple400", BaseColors.wdsPurple400),
            ColorItem("wdsPurple500", BaseColors.wdsPurple500),
            ColorItem("wdsPurple600", BaseColors.wdsPurple600),
            ColorItem("wdsPurple700", BaseColors.wdsPurple700),
            ColorItem("wdsPurple800", BaseColors.wdsPurple800),
        )
    ),
    ColorSection(
        title = "Brown",
        colors = listOf(
            ColorItem("wdsBrown50", BaseColors.wdsBrown50),
            ColorItem("wdsBrown75", BaseColors.wdsBrown75),
            ColorItem("wdsBrown100", BaseColors.wdsBrown100),
            ColorItem("wdsBrown200", BaseColors.wdsBrown200),
            ColorItem("wdsBrown300", BaseColors.wdsBrown300),
            ColorItem("wdsBrown400", BaseColors.wdsBrown400),
            ColorItem("wdsBrown500", BaseColors.wdsBrown500),
            ColorItem("wdsBrown600", BaseColors.wdsBrown600),
            ColorItem("wdsBrown700", BaseColors.wdsBrown700),
            ColorItem("wdsBrown800", BaseColors.wdsBrown800),
        )
    ),
    ColorSection(
        title = "Cream",
        colors = listOf(
            ColorItem("wdsCream50", BaseColors.wdsCream50),
            ColorItem("wdsCream75", BaseColors.wdsCream75),
            ColorItem("wdsCream100", BaseColors.wdsCream100),
            ColorItem("wdsCream150", BaseColors.wdsCream150),
            ColorItem("wdsCream200", BaseColors.wdsCream200),
            ColorItem("wdsCream300", BaseColors.wdsCream300),
            ColorItem("wdsCream400", BaseColors.wdsCream400),
            ColorItem("wdsCream500", BaseColors.wdsCream500),
            ColorItem("wdsCream600", BaseColors.wdsCream600),
            ColorItem("wdsCream700", BaseColors.wdsCream700),
            ColorItem("wdsCream800", BaseColors.wdsCream800),
        )
    ),
)

@Composable
private fun ColorSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = SECTION_TOP_PADDING)
    ) {
        Text(
            text = title.uppercase(),
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = WdsTheme.colors.colorContentDeemphasized,
            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING)
        )
        HorizontalDivider(
            color = WdsTheme.colors.colorDivider,
            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING)
        )
    }
}

@Composable
private fun ColorRow(
    name: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color = WdsTheme.colors.colorContentDefault,
            modifier = Modifier.weight(1f)
        )
        
        Box(
            modifier = Modifier
                .size(width = COLOR_SWATCH_WIDTH, height = COLOR_SWATCH_HEIGHT)
                .background(
                    color = color,
                    shape = RoundedCornerShape(COLOR_SWATCH_CORNER_RADIUS)
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorsScreen(
    onNavigateBack: () -> Unit
) {
    val colors = WdsTheme.colors
    val semanticColors = remember(colors) {
        getSemanticColorSections(colors)
    }
    
    val baseColors = remember { baseColorSections }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Colors",
                        color = WdsTheme.colors.colorContentDefault
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = WdsTheme.colors.colorContentDefault
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WdsTheme.colors.colorBackgroundWashInset
                )
            )
        },
        containerColor = WdsTheme.colors.colorBackgroundWashInset
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Semantic Colors Section
            item(key = "semantic_header") {
                Text(
                    text = "Semantic Colors",
                    style = MaterialTheme.typography.titleLarge,
                    color = WdsTheme.colors.colorContentDefault,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING)
                )
            }

            semanticColors.forEach { section ->
                item(key = "semantic_${section.title}") {
                    ColorSectionHeader(title = section.title)
                }
                
                items(
                    items = section.colors,
                    key = { colorItem -> "semantic_${section.title}_${colorItem.name}" }
                ) { colorItem ->
                    ColorRow(
                        name = colorItem.name,
                        color = colorItem.color
                    )
                }
            }

            // Base Colors Section
            item(key = "base_header") {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "WDS Base Colors",
                    style = MaterialTheme.typography.titleLarge,
                    color = WdsTheme.colors.colorContentDefault,
                    modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING)
                )
            }

            baseColors.forEach { section ->
                item(key = "base_${section.title}") {
                    ColorSectionHeader(title = section.title)
                }
                
                items(
                    items = section.colors,
                    key = { colorItem -> "base_${section.title}_${colorItem.name}" }
                ) { colorItem ->
                    ColorRow(
                        name = colorItem.name,
                        color = colorItem.color
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorsScreenPreview() {
    WdsTheme {
        ColorsScreen(onNavigateBack = {})
    }
}
