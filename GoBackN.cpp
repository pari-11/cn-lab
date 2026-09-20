#include <iostream>
using namespace std;

int main() {
    int n, window, lost;

    cout << "Enter number of frames: ";
    cin >> n;

    cout << "Enter window size: ";
    cin >> window;

    cout << "Enter lost frame: ";
    cin >> lost;

    cout << "\n--- Go-Back-N ---\n";

    for (int i = 0; i < n; i += window) {
        int end = min(i + window, n);

        cout << "Sending frames: ";
        for (int j = i; j < end; j++) {
            cout << j << " ";
        }
        cout << endl;

        if (lost >= i && lost < end) {
            cout << "Frame " << lost << " is lost!\n";

            cout << "Retransmitting: ";
            for (int j = lost; j < end; j++) {
                cout << j << " ";
            }
            cout << endl;

            lost = -1;
        }
    }

    return 0;
}