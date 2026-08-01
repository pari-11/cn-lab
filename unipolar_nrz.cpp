#include <iostream>
#include <string>
using namespace std;

int main() {
    string data;

    cout << "Enter binary data: ";
    cin >> data;

    cout << "\nUnipolar NRZ Encoding:\n";

    for(char bit : data) {
        if(bit == '1')
            cout << "+V ";
        else
            cout << "0 ";
    }

    return 0;
}