#!/bin/bash

# Check if a number was provided
if [ -z "$1" ]
then
  echo "Please provide a number."
  exit 1
fi

# Ensure the number is positive
if [ "$1" -le 0 ]
then
  echo "Number must be positive."
  exit 1
fi

# Create the new files based on template
cp src/DayXX.kt src/Day"$1".kt || exit 1
touch src/Day"$1".txt src/Day"$1"_test.txt || exit 1

echo "Files created successfully."

# Add new files to Git
git add src/Day"$1".kt src/Day"$1".txt src/Day"$1"_test.txt

echo "Files added to Git."
