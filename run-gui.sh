#!/bin/bash
# Quick launcher for ONE% Game Show (Slinky / React)

set -e

echo "🎮  Building ONE% Game Show…"
echo ""

sbt fastOptJS::webpack

BUNDLE_DIR="target/scala-2.13/scalajs-bundler/main"

# Copy static assets next to the bundle
cp src/main/resources/index.html "$BUNDLE_DIR/"
cp src/main/resources/style.css  "$BUNDLE_DIR/"

echo ""
echo "✅  Build complete!"
echo "📂  Open in browser:  file://$(cd "$BUNDLE_DIR" && pwd)/index.html"
echo ""
echo "Or start a local server:"
echo "   cd $BUNDLE_DIR && python3 -m http.server 8080"
echo "   Then open http://localhost:8080"
