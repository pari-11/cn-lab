#include <iostream>
#include <string>
using namespace std;

int main() {
    string data;
    cout << "Enter binary data: ";
    cin >> data;

    cout << "\nManchester Encoding:\n";

    for (char bit : data) {

        if (bit == '0')
            cout << "[+V,-V] ";

        else
            cout << "[-V,+V] ";
    }

    return 0;
}