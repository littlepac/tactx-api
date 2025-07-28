import React, {useEffect, useState} from 'react';
import './App.css';
import {GoogleOAuthProvider} from "@react-oauth/google";
import HomePage from "./HomePage";
import {getMainView, getUserDetail, getUserPick, getUserState, login, removePick, setPick} from "./api";
import {DayStocksView, UserDetails} from "./models";
import Cookies from 'js-cookie';

const clientId = "636074117081-gfhjtuf0ie9fes2a1tdug4afsumd3j2m.apps.googleusercontent.com";

const App = () => {
    const cookie = Cookies.get('hundred_bucks_session');
    const [loading, setLoading] = useState<boolean>(true);
    const [mainView, setMainView] = useState<DayStocksView | null>(null);
    const [userDetails, setUserDetails] = useState<UserDetails | null>(null);
    const [userPick, setUserPick] = useState<number | null>(null);
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
            window.location.reload()
        }, 250))}/>
    </GoogleOAuthProvider>

    if (mainView) {
        const tradeDate = mainView.tradeDate;
        return <div>
            {"TradeDate: " + tradeDate}
            {"UserDetails: " + JSON.stringify(userDetails)}
            {
                mainView.stocks.map(
                    it =>
                        <tr>
                            <button onClick={() => setPick(tradeDate, it.id).then(setUserPick)}>{(userPick === it.id ? "[Chosen]" : "") + JSON.stringify(it)}</button>
                        </tr>
                )
            }
            <button onClick={() => removePick(tradeDate).then(setUserPick)}>{(userPick === null ? "[Chosen]" : "") + " Remove pick"}</button>
            {loggedIn ? null : logInOption}</div>
    } else {
        return "nothing to show"
    }
}

export default App;
