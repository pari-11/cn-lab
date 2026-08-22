#include <iostream>
#include <string>
using namespace std;

int main() {
    string data;
    cout << "Enter binary data: ";
    cin >> data;

    string start = "-V";

    cout << "\nNRZ-I Encoding:\n";

    for (char bit : data) {

        if (bit == '1') {
            if (start == "-V")
                start = "+V";
            else
                start = "-V";
        }

        cout << start << " ";
    }

    return 0;
}