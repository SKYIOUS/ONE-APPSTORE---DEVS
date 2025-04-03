#!/bin/bash

# Build script for ONE App Store - builds all platforms

set -e  # Exit on error

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Building ONE App Store for all platforms..."

# Build Web (Wasm)
echo "-----------------------------------"
echo "Building Web (Wasm) version..."
echo "-----------------------------------"
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Build Desktop
echo "-----------------------------------"
echo "Building Desktop version..."
echo "-----------------------------------"
./gradlew :composeApp:run

# Build Android
echo "-----------------------------------"
echo "Building Android version..."
echo "-----------------------------------"
./gradlew :composeApp:assembleDebug

echo "-----------------------------------"
echo "All builds completed successfully!"
echo "-----------------------------------"
echo "Build outputs:"
echo "- Web: ./composeApp/build/dist/wasmJs/developmentExecutable"
echo "- Desktop: ./composeApp/build/compose/binaries/main"
echo "- Android: ./composeApp/build/outputs/apk/debug/composeApp-debug.apk" 