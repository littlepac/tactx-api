import React, {useEffect, useState} from 'react';
import './App.css';
import {GoogleOAuthProvider} from "@react-oauth/google";
import HomePage from "./HomePage";
import {getMainView, getUserDetail, getUserPick, getUserState, login} from "./api";
import {DayStocksView, UserDetails, UserPick} from "./models";
import Cookies from 'js-cookie';

const clientId = "636074117081-gfhjtuf0ie9fes2a1tdug4afsumd3j2m.apps.googleusercontent.com";

const App = () => {
    const cookie = Cookies.get('hundred_bucks_session');
    const [loading, setLoading] = useState<boolean>(true);
    const [mainView, setMainView] = useState<DayStocksView | null>(null);
    const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
    const [userPick, setUserPick] = useState<UserPick | null>(null);
    useEffect(() => {
        getMainView()
            .then(async mainView => {
                setMainView(mainView);
                if (cookie) {
                    await Promise.all([getUserDetail().then(setUserDetails), getUserPick(mainView.tradeDate).then(setUserPick)])
                }
                setLoading(false);
            })
        ;
    }, []);

    const loggedIn = userDetails !== null;

    const logInOption = <GoogleOAuthProvider clientId={clientId}>
        <HomePage redirect={(credential) => login(credential, () => setTimeout(() => {
            // window.location.reload()
        }, 250))}/>
    </GoogleOAuthProvider>

    return <div>
        {JSON.stringify(mainView)}
        {loggedIn ? null : logInOption}</div>


}

export default App;
