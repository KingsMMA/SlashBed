# SlashBed
A fabric 1.21.4 server-side mod that adds the /bed command.  This command lets players teleport to their bed.

![Screenshot of teleporting title](https://github.com/user-attachments/assets/7e25d7f8-4927-4452-8037-eb2c3eb8e284)

### Commands
- `/bed` - Teleport to the player's bed spawn location.
- `/slashbed reload` - Reloads the config.

### Default config
```json
{
  "alreadyTeleportingMessage": "You are already being teleported to your bed.",
  "teleportedMessage": "Teleported to bed spawn location.",
  "teleportCancelled": "Teleport cancelled because you moved.",
  "teleportingTitle": "Teleporting to bed, please stand still...",
  "cancelOnMove": true,
  "movementAllowance": 0.5,
  "delay": 3
}
```
