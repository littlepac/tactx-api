import React, {useState, useEffect} from 'react';
import './output.css'
import { StockGame } from './components/StockGame';
import { Leaderboard } from './components/Leaderboard';
import { Login } from './components/Login';
import {UserSettings} from "./components/UserSettings";
import Cookies from 'js-cookie';
import {DayStocksView, Stock, UserPick} from "./models";
import {getMainView, getUserDetail, getUserPick, login, removePick, setPick, updateUsername} from './api';
import {GoogleOAuthProvider} from "@react-oauth/google";

const clientId = "636074117081-gfhjtuf0ie9fes2a1tdug4afsumd3j2m.apps.googleusercontent.com";

type View = 'game' | 'leaderboard' | 'login' | 'settings';

interface User {
    username: string;
    email: string;
}

export default function App() {
    const [currentView, setCurrentView] = useState<View>('game');
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [username, setUsername] = useState<string | null>(null);
    const [balance, setBalance] = useState<number>(100);
    const [userPick, setUserPick] = useState<UserPick | null>(null);
    const [mainView, setMainView] = useState<DayStocksView | null>(null);
    const [loadingView, setLoadingView] = useState<boolean>(true);

    const showGame = () => {
        setCurrentView('game');
    };

    const showLeaderboard = () => {
        setCurrentView('leaderboard');
    };

    const showLogin = () => {
        setCurrentView('login');
    };

    const showSettings = () => {
        if (isAuthenticated) {
            setCurrentView('settings');
        }
    };

    const handleLogout = () => {
        Cookies.remove('hundred_bucks_session');
        setTimeout(() => window.location.reload(), 250);
    };

    const handleUsernameChange = (newUsername: string) => {
        if (newUsername) {
            updateUsername(newUsername).then(setUsername)
        }
    };

    const cookie = Cookies.get('hundred_bucks_session');
        useEffect(() => {
            getMainView().then(async mainView => {
                setMainView(mainView)
                if (cookie) {
                    Promise.all([getUserDetail().then((user) => {
                        if (user) {
                            setUsername(user.userName);
                            setBalance(user.currentAccountBalance)
                            setIsAuthenticated(true)
                        }
                    }), getUserPick(mainView.tradeDate).then(setUserPick)]).finally(() => setLoadingView(false))
                } else {
                    setLoadingView(false)
                }
            });
    }, []);

    if (loadingView) {
        return <div>Loading, please wait</div>
    }

    return (
        <>
            <GoogleOAuthProvider clientId={clientId}>
                {currentView === 'game' && (
                    <StockGame
                        balance={balance}
                        onShowLeaderboard={showLeaderboard}
                        onShowSettings={showSettings}
                        onShowLogin={showLogin}
                        isAuthenticated={isAuthenticated}
                        username={username}
                        mainView={mainView}
                        userPick={userPick}
                        onSelectPick={(tradeDate, pickId) => setPick(tradeDate, pickId).then(setUserPick).catch((e) => console.log(e))}
                        onRemovePick={(tradeDate) => removePick(tradeDate).then(setUserPick).catch((e) => console.log(e))}
                    />
                )}
                {currentView === 'leaderboard' && (
                    <Leaderboard onBack={showGame}/>
                )}
                {currentView === 'login' && (
                    <Login onGoogleLoginSuccess={token => login(token, () => {
                        setTimeout(() => window.location.reload(), 250)
                    })} />
                )}
                {currentView === 'settings' && isAuthenticated && username && (
                    <UserSettings
                        onBack={showGame}
                        onLogout={handleLogout}
                        currentUsername={username}
                        onUsernameChange={handleUsernameChange}
                    />
                )}
            </GoogleOAuthProvider>
        </>
    );
}