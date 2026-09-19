#include <iostream>
#include <string>
using namespace std;

// Perform modulo-2 division
string divide(string data, string generator) {
    int n = generator.length();

    for (int i = 0; i <= data.length() - n; i++) {

        // Only divide if current bit is 1
        if (data[i] == '1') {

            for (int j = 0; j < n; j++) {
                // XOR the bits
                data[i + j] =
                    (data[i + j] == generator[j]) ? '0' : '1';
            }
        }
    }

    // Return the remainder
    return data.substr(data.length() - (n - 1));
}

int main() {

    string data, generator;

    cout << "Enter data bits: ";
    cin >> data;

    cout << "Enter generator bits: ";
    cin >> generator;

    int zeros = generator.length() - 1;

    // Append zeros to data
    string appendedData = data + string(zeros, '0');

    // Find CRC remainder
    string remainder = divide(appendedData, generator);

    cout << "CRC remainder: " << remainder << endl;

    // Transmitted data = original data + CRC
    string transmittedData = data + remainder;

    cout << "Transmitted codeword: "
         << transmittedData << endl;

    // Receiver side
    string receivedData;

    cout << "\nEnter received codeword: ";
    cin >> receivedData;

    // Divide received codeword by generator
    string checkRemainder = divide(receivedData, generator);

    cout << "Remainder at receiver: "
         << checkRemainder << endl;

    // Check for error
    bool error = false;

    for (char c : checkRemainder) {
        if (c != '0') {
            error = true;
            break;
        }
    }

    if (error) {
        cout << "Error detected!" << endl;
    }
    else {
        cout << "No error detected." << endl;
    }

    return 0;
}