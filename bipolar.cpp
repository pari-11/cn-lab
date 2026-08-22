#include <iostream>
#include <string>
using namespace std;

int main() {
    string data;
    cout << "Enter binary data: ";
    cin >> data;
    bool positive = true;

    cout << "\nBipolar (AMI) Encoding:\n";
    for (char bit : data) {

        if (bit == '0')
            cout << "0V ";
        else {
            if (positive)
                cout << "+V ";
            else
                cout << "-V ";
            positive = !positive;
        }
    }

    return 0;
}