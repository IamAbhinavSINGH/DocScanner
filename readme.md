# DocScanner

DocScanner is an Android application developed using Kotlin and Jetpack Compose that allows users to create, manage, and view PDFs using Google's ML Kit API. This app features auto-cropping, tint removal, corner adjustment, a built-in PDF viewer, a search functionality, and a bin for temporarily storing deleted PDFs.

## Features

- **Create PDFs**: Use Google's ML Kit API to scan documents with features like auto-cropping, tint removal, and corner adjustment.
- **PDF Viewer**: View PDFs directly within the app.
- **Search**: Easily search through all created PDFs.
- **Search History**: Keep track of all previously searched PDFs.
- **Bin**: Temporarily store deleted PDFs for 30 days, with the option to restore them before they are permanently deleted.
- **Intuitive UI**: Enjoy a clean and user-friendly interface designed with Jetpack Compose.

## Screenshots

![Screenshot1](path_to_screenshot1)
![Screenshot2](path_to_screenshot2)
![Screenshot3](path_to_screenshot3)

## Installation

To install the app, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/IamAbhinavSINGH/DocScanner.git
Open the project in Android Studio.
Build and run the app on your Android device or emulator.

## Usage
Scan a Document: Open the app and use the scanning feature to capture a document. The app will automatically crop, remove tints, and adjust corners using ML Kit.

- View PDF: Access the created PDFs in the built-in PDF viewer.
- Search PDFs: Use the search functionality to find specific PDFs by keywords.
- Manage PDFs: Move unwanted PDFs to the bin where they will be stored for 30 days before permanent deletion. You can restore PDFs from the bin within this period.

## Permissions
The app requires the following permissions:

- Storage: To save and manage PDF files.

## Libraries Used
- Jetpack Compose
- Google ML Kit

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes. Ensure your code follows the existing coding style and includes appropriate tests.

- Fork the repository.

- Create a new branch:
> git checkout -b feature/YourFeature

- Commit your changes:
> git commit -m 'Add some feature'

- Push to the branch:
> git push origin feature/YourFeature

- Open a pull request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
If you have any questions or suggestions, feel free to reach out.

## Acknowledgements
- Google ML Kit
- Jetpack Compose
- PDF Library
