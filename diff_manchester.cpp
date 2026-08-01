#include <iostream>
#include <string>
using namespace std;

int main() {

    string data;
    cout << "Enter binary data: ";
    cin >> data;

    string firstHalf = "+V";
    string secondHalf;

    cout << "\nDifferential Manchester Encoding:\n";

    for (char bit : data) {

        // Beginning transition for 0
        if (bit == '0') {
            if (firstHalf == "+V")
                firstHalf = "-V";
            else
                firstHalf = "+V";
        }

        // Mid-bit transition
        if (firstHalf == "+V")
            secondHalf = "-V";
        else
            secondHalf = "+V";

        cout << "[" << firstHalf << "," << secondHalf << "] ";

        // Next bit starts where previous ended
        firstHalf = secondHalf;
    }

    return 0;
}