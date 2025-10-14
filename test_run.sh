#!/bin/bash
# Simulate user interaction with the PC Part Picker application
{
  sleep 1; echo ""          # Press Enter (welcome screen)
  sleep 1; echo "2"          # Select "Browse Parts"
  sleep 1; echo "1"          # Select "CPUs"
  sleep 1; echo "v 1"        # View first CPU details
  sleep 1; echo ""          # Press Enter to go back
  sleep 1; echo "b"          # Back to categories
  sleep 1; echo "0"          # Back to main menu
  sleep 1; echo "6"          # Select "About"
  sleep 1; echo ""          # Press Enter
  sleep 1; echo "0"          # Exit
} | timeout 30 mvn -q exec:java -Dexec.mainClass="de.dhbw.tinf23b3.pcpartpicker.Main" 2>&1
