    #include <iostream>
#include <string>
using namespace std;

int main() {
    string code;

    cout << "Enter 7-bit Hamming code: ";
    cin >> code;

    // code = D7 D6 D5 P4 D3 P2 P1
    // Position numbers from right to left:
    // 7 6 5 4 3 2 1

    // Convert string positions to Hamming positions
    int bit[8];

    for (int i = 1; i <= 7; i++) {
        bit[i] = code[7 - i] - '0';
    }

    // Check P1: positions 1, 3, 5, 7
    int p1 = bit[1] ^ bit[3] ^ bit[5] ^ bit[7];

    // Check P2: positions 2, 3, 6, 7
    int p2 = bit[2] ^ bit[3] ^ bit[6] ^ bit[7];

    // Check P4: positions 4, 5, 6, 7
    int p4 = bit[4] ^ bit[5] ^ bit[6] ^ bit[7];

    // Syndrome = P4 P2 P1
    int errorPosition = p4 * 4 + p2 * 2 + p1;

    if (errorPosition == 0) {
        cout << "No error detected." << endl;
    }
    else {
        cout << "Error detected at position: "
             << errorPosition << endl;

        // Correct the error
        bit[errorPosition] ^= 1;

        cout << "Error corrected." << endl;
    }

    // Convert back to D7 D6 D5 P4 D3 P2 P1
    string correctedCode = "";

    for (int i = 7; i >= 1; i--) {
        correctedCode += char(bit[i] + '0');
    }

    cout << "Corrected code: " << correctedCode << endl;

    return 0;
}



