import {DayStocksView, UserDetails, UserPick} from "./models";

const url = "https://tactx-api-61597259690.us-central1.run.app";

export const login = (credential: string | undefined, onSuccess: () => void) => {
    fetch(`${url}/api/auth/google`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include', // important to send/receive cookies
        body: JSON.stringify({googleOauthCredential: credential}),
    }).then(response => {
        if (response.ok) {
            onSuccess();
        }
    });
};

export const updateUsername = (username: String) => {
    return fetch(`${url}/api/user/rename?to=${username}`, {
        method: 'PUT',
        credentials: 'include',
    }).then(response =>
        response.text())
};

export const getMainView = () : Promise<DayStocksView> => {
    return fetch(`${url}/api/stocks/current`, {
        method: 'GET',
        credentials: 'include',
    }).then(response =>
        response.json()).then(((payload: DayStocksView) => payload))
};

export const getUserPick = (tradeDate: string): Promise<UserPick | null> => {
    return fetch(`${url}/api/pick/${tradeDate}`, {
        method: 'GET',
        credentials: 'include',
    }).then((response) => response?.json())
};

export const getUserDetail = (): Promise<UserDetails | null> => {
    return fetch(`${url}/api/user/details`, {
        method: 'GET',
        credentials: 'include',
    }).then(response => response.json()).then((((payload: UserDetails) => payload))).catch(() => null);
};

export const setPick = (tradeDate: string, pickId: number | null): Promise<UserPick> => {
    console.log(tradeDate)
    return fetch(`${url}/api/pick/${tradeDate}/${pickId}`, {
        method: 'PUT',
        credentials: 'include',
    }).then((response) => response.json())
};

export const removePick = (tradeDate: string): Promise<UserPick> => {
    return fetch(`${url}/api/pick/${tradeDate}`, {
        method: 'DELETE',
        credentials: 'include',
    }).then(response => response.json())
};
