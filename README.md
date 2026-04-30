<center>

[![Modrinth Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/modrinth_vector.svg)](https://modrinth.com/project/zaVwh7an)
[![Github Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/github_vector.svg)](https://github.com/Solmeye/Eslium)
[![Discord Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/discord-singular_vector.svg)](https://discord.gg/FVq3j5heAc)

[![Fabric Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/quilt_vector.svg)](https://quiltmc.org/)
[![NeoForge Icon](https://raw.githubusercontent.com/Solmeye/Mirror-Anticheat/2b0ed26bc9fd49f2f1c6b8aa289dbb81cd0db956/neoforge.svg)](https://neoforged.net/)
[![Forge Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/forge_vector.svg)](files.minecraftforge.net)

</center>

___

## What's Eslium ?
Eslium is a project to predict more things client-side.
For example, if you swap items with your secondary hand, you must wait for the server to exchange it for you
This mod fixes that and therefore compensates for the ping.

## Predictions implemented

<details>
<summary>Swap</summary>

When pressing the key to exchange the item from the main hand to the offhand, outside of an inventory, the swap is predicted

</details>


<details>
<summary>Inventory</summary>

When the inventory is resynchronized by the server, if the inventory indicated by the server corresponds to that which the client previously had, the synchronization is canceled

This avoids rollbacks due to delayed resynchronization

</details>


<details>
<summary>Minecarts</summary>

When using any type of minecart on a rail, the spawn of the minecart is predicted

</details>


## Predictions Coming Soon™ :
- End Crystal
- Cooldowns
- Consumables
- Elytra
- Firework rocket
- Entity pose
- Wind charge
- Jukebox
- Crossbow
- Pickup entities
- Trident
- Weapons
- Jukebox
- Bottle o' Enchanting
- Experience
- Ender Pearls
- Potions
- Pick Block
- Cactus damage
- Bed
- Fishing Rod
- Lead
- Boat
- Snowball
- Spawn Eggs
- Egg
- Armor Stand
- Note block
- Item frame
- Dyes
- Interfaces
- Void damage
- Game mode change
- Potion Effects
- Tchat
- Sounds
- Crafting
- Scaffolding
- Knockback
- Fall damage
- Interaction with blocks
- Interaction with entities
- Interaction with items

## Disclaimer
Versions for 1.21.x are deprecated. I strongly recommend not using them.
This mod does not give any advantages, it is not a cheat. It only compensates for the ping.

Allowed on servers such as:
- `PvPClub`
- `FadedMC`
- `Turtled`
- `CatPvP`

Versions 4.0+ are designed not to trigger anticheats.
However, a bug is not inevitable, so it is still recommended to request authorization for this mod before using it on a server.

## FAQ
### How it works ?
This mod uses mixins to inject code into Minecraft code. During certain actions, if implemented, the behavior of a vanilla server is replicated on the client side. 

### Is this likely to break the desynchronization of the client-server tick loop?
Not really. This desynchronization is replicated by an artificial time delay of half a tick.

## Compatibility
Eslium should work on any client.

## Support me!
Join my Hardcore Minecraft server: `80.91.86.136:51965`
