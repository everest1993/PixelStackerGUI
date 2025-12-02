# PixelStackerGUI 

**PixelStackerGUI** is the graphical user interface 
for **PixelStackerCLI**, focused on providing a 
**smooth and simple workflow and user experience** 
for photographic stacking.

## Requirements

- Java 24 o superiore
- Maven
- JavaFX
- [PixelStackerCLI](https://github.com/everest1993/PixelStackerCLI) installed and available on your system

## Installation

Clone the repository and run:

```bash
git clone https://github.com/everest1993/PixelStackerGUI.git

cd PixelStackerGUI

# run the GUI
mvn javafx:run
```

## Best Practices for Input Images

To achieve optimal results with PixelStackerCLI, follow these guidelines when capturing source images:

1. **Use a tripod** – All photos must be taken from a fixed position without moving the camera or lens between shots.
2. **Same framing and focal length** – Avoid zooming or reframing between exposures.
3. **Identical dimensions** – All images should have the same resolution and aspect ratio.
4. **Consistent exposure settings** – Use manual mode (M) to keep ISO, aperture, and shutter speed fixed when possible.
5. **Sufficient image count** – For noise reduction, use at least 4 images (8 or more recommended).
6. **RAW format needed** – Provides the highest dynamic range and color depth.
7. **Avoid in-camera processing** – Disable noise reduction, HDR, or automatic corrections.

## Contributing

Contributions are welcome! You can help by:

- Reporting bugs via issues
- Requesting new features via issues
- Submitting pull requests

```bash
# Process for submitting a pull request
git checkout -b feature/feature-name
git commit -m "Description of changes"
git push origin feature/feature-name
```

- Open a pull request on the main repository

**Guidelines**

- Keep code style consistent
- Include tests for new features when possible
- Update the README if adding commands or significant changes

## Support

For questions, bug reports, or requests, contact:

**pixelstackercli.project@gmail.com**