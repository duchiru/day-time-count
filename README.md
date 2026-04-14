# Day Time Counter

**Day Time Counter** is a lightweight utility mod that adds a clean, minimal display to the top-left corner of your
screen, showing the current **day** and **in-game time**.

---

## Features

* **Real-time Day & Time Display**:
  Instantly see the current in-game day and time.

* **Milestone Celebrations**:
  When you reach survival milestones (10, 50, 100 and so on), there will be a "cheer" screen title and a celebratory
  sound effect to mark your progress.

* **Fully Customizable**:
  Adjust the display's position, font size, and color to fit your preferences.

---

## Requirements

* Fabric API 0.135.1 or higher

---

## Configuration

The mod's configuration file is located at `config/daytimecount.json`.

```json
{
  "tracker_style": "default (default | compact | day_only | time_only)",
  "tracker_position": "top_right (top_left | top_right | bottom_left | bottom_right | hotbar)",
  "tracker_text_color": "#FFFFFF (#RRGGBB or #AARRGGBB)",
  "tracker_text_scale": "1.0 (0.5 to 3.0)",
  "milestones": {
    "100": {
      "title": "Wow, 100-Day Challenge!",
      "subtitle": "It\u0027s time to show off your world!",
      "title_color": "AQUA",
      "subtitle_color": "GREEN",
      "sound": "ui.toast.challenge_complete"
    }
  }
}
```

`tracker_text_color` supports either `#RRGGBB` (alpha defaults to `FF`) or `#AARRGGBB`.

---

## Preview

![main feature preview](https://cdn.modrinth.com/data/KapwD8fJ/images/60d5db51e31d029a06c139ad33c23bc0cdf5fbc8.png)