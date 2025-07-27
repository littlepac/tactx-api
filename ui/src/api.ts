import {DayStocksView, UserDetails, UserPick} from "./models";

export const login = (credential: string | undefined, onSuccess: () => void) => {
    fetch('https://localhost:8443/api/auth/google', {
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

export const getUserState = () => {
    return fetch('https://localhost:8443/api/user-state', {
        method: 'GET',
        credentials: 'include',
    }).then(response =>
        response.json()).then(((payload: {loggedIn : boolean}) => payload.loggedIn))
};

export const getMainView = () : Promise<DayStocksView> => {
    return fetch('https://localhost:8443/api/stocks/current', {
        method: 'GET',
        credentials: 'include',
    }).then(response =>
        response.json()).then(((payload: DayStocksView) => payload))
};

export const getUserPick = (tradeDate: string): Promise<UserPick> => {
    return fetch(`https://localhost:8443/api/pick/${tradeDate}`, {
        method: 'GET',
        credentials: 'include',
    }).then(response => response.json()).then((((payload: UserPick) => payload)));
};

export const getUserDetail = (): Promise<UserDetails> => {
    return fetch(`https://localhost:8443/api/user`, {
        method: 'GET',
        credentials: 'include',
    }).then(response => response.json()).then((((payload: UserDetails) => payload)));
};
