# FuturePack Research Patcher

[![](https://img.shields.io/badge/Minecraft-1.18.2-brightgreen)](https://www.minecraft.net/)
[![](https://img.shields.io/badge/Forge-40.3.11-orange)](https://files.minecraftforge.net/)
[![](https://img.shields.io/badge/FuturePack-33.0.7547+-blue)](https://www.curseforge.com/minecraft/mc-mods/futurepack)

A Minecraft Forge mod that allows modpack makers to override FuturePack research configurations using the datapack system.

## Features

- Override any FuturePack research using datapacks
- Hot-reload support with `/reload` command
- KubeJS integration support
- Custom `/fpreload` command for manual research reload

## Installation

1. Download the latest version from [Releases](https://github.com/你的用户名/FPResearchPatcher/releases)
2. Place the JAR file in your `mods` folder
3. Ensure FuturePack is also installed

## Usage

### Method 1: KubeJS Integration (Recommended)
Place your research override files in:
kubejs/data/[namespace]/futurepack_research/[page_name].json
### Method 2: Standalone Datapack
Create a datapack structure:
datapack_name/
├── pack.mcmeta
└── data/
└── [namespace]/
└── futurepack_research/
└── [page_name].json

### Available Pages
- `main.json` - Main research page
- `story.json` - Story researches
- `production.json` - Production researches
- `energy.json` - Energy researches
- `logistic.json` - Logistics researches
- `chips.json` - Chip researches
- `deco.json` - Decoration researches
- `space.json` - Space researches
- `tools.json` - Tool researches
## Example
Override industrial furnace position in `production.json`:
```json
[
  {
    "id": "production.industrieofen",
    "page": "production",
    "x": 0,
    "y": 0,
    "parents": [],
    "aspects": [],
    "level": 0,
    "time": 0,
    "need": [],
    "icon": {
      "name": "futurepack:industrial_furnace"
    },
    "enables": [
      {
        "name": "futurepack:industrial_furnace"
      }
    ]
  }
]
```
## Commands
1. `/reload` - Reload all datapacks
2. `/fpreload`- Force reload FuturePack research system

## Building from Source
```
git clone https://github.com/yourname/FPResearchPatcher.git
cd FPResearchPatcher
./gradlew build
```
The compiled JAR will be in build/libs/

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Credits
- FuturePack by MCenderdragon
- Built with MinecraftForge
- Uses SpongePowered Mixin
