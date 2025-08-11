import React, { useState } from 'react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import {Trophy, Settings, RotateCcw, Check} from 'lucide-react';
import { HundredBucksLogo } from './HundredBucksLogo';
import {DayStocksView, Stock, UserPick} from "../models";

interface StockGameProps {
    balance: number;
    onShowLeaderboard: () => void;
    onShowSettings: () => void;
    onShowLogin: () => void;
    isAuthenticated: boolean;
    username: string | null;
    mainView: DayStocksView | null;
    userPick: UserPick | null;
    onSelectPick: (tradeDate: string, pickId: number | null) => void;
    onRemovePick: (tradeDate: string) => void;
}

export function StockGame({ onShowLeaderboard, onShowSettings, onShowLogin, isAuthenticated, username, balance , mainView, userPick, onSelectPick, onRemovePick}: StockGameProps) {
    if (!mainView) {
        return <div>Loading main view</div>
    }
    const {tradeDate, stocks, tradeable} = mainView;
    const handleStockSelect = (stockId: number | null) => {
        // If user is not authenticated, redirect to login
        if (!isAuthenticated) {
            onShowLogin();
            return;
        }
        onSelectPick(tradeDate, stockId)
    };

    const getSelectedStockName = () => {
        const pickId = userPick?.pickId;
        if (pickId === null) return 'Skipping..';
        const stock = stocks?.find(s => s.id === pickId);
        return stock ? stock.ticker : '';
    };

    // Get current date in a readable format
    const getCurrentDate = (date: string) => {
        return new Date(date).toLocaleDateString('en-US', {
            weekday: 'long',
            month: 'long',
            day: 'numeric'
        });
    };

    const selectedStock = userPick !== null;
    return (
        <div className="min-h-screen bg-background flex items-center justify-center p-4">
            <div className="w-full max-w-md mx-auto space-y-6">
                <div className="text-center space-y-3">
                    <div className="flex items-center justify-between">
                        {isAuthenticated ? (
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={onShowSettings}
                                className="p-2"
                            >
                                <Settings className="w-4 h-4"/>
                            </Button>
                        ) : (
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={onShowLogin}
                                className="p-2 text-primary"
                            >
                                Sign In
                            </Button>
                        )}
                        <HundredBucksLogo/>
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={onShowLeaderboard}
                            className="p-2"
                        >
                            <Trophy className="w-4 h-4"/>
                        </Button>
                    </div>

                    <div className="space-y-1">
                        <p className="text-sm text-muted-foreground">{tradeDate && getCurrentDate(tradeDate)}</p>
                        {isAuthenticated && (
                            <p className="text-xs text-muted-foreground">Welcome back, {username}!</p>
                        )}
                        <p className="text-muted-foreground">Current Balance</p>
                        <div className="text-3xl font-medium text-primary">
                            ${balance.toFixed(2)}
                        </div>
                    </div>
                </div>

                {/* Selection status banner */}
                {selectedStock && isAuthenticated && (
                    <Card className="p-4 text-center space-y-3 bg-green-50 border-green-200">
                        <div className="flex items-center justify-center gap-2">
                            <Check className="w-4 h-4 text-green-600"/>
                            <span className="text-sm text-green-800 font-medium">
                                Current choice: {getSelectedStockName()}
                            </span>
                        </div>
                    </Card>
                )}

                {/* Auth prompt for unauthenticated users */}
                {!isAuthenticated && (
                    <Card className="p-4 text-center space-y-3 bg-blue-50 border-blue-200">
                        <p className="text-sm text-blue-800">
                            Sign in to start playing and track your progress!
                        </p>
                        <Button onClick={onShowLogin} size="sm" className="bg-blue-600 hover:bg-blue-700">
                            Sign In to Play
                        </Button>
                    </Card>
                )}

                {/* Instructions */}
                <div className="text-center space-y-1">
                    <p className="text-muted-foreground">
                        {tradeable ? (selectedStock ? "Change your choice before market open" : "Choose your investment for today") : "Today's picking session has ended"}
                    </p>
                    <p className="text-xs text-muted-foreground">
                        {!isAuthenticated
                            ? 'Sign in to make your selection'
                            : `Selections lock from 9:25a.m EST`
                        }
                    </p>
                </div>

                {/* Stock options */}
                <div className="space-y-3">
                    {(stocks || []).map((stock) => {
                        const {id, ticker, name, previousOpen, previousClose, reasonForSelection} = stock
                        const change = previousClose - previousOpen;
                        const changePercent = ((previousClose / previousOpen) - 1) * 100;
                        const isSelected = userPick?.pickId === stock.id;
                        return (
                            <Card
                                key={stock.id}
                                className={`p-4 transition-all duration-200 ${
                                    isSelected
                                        ? 'border-green-500 bg-green-50 shadow-md'
                                        : (!isAuthenticated || !tradeable)
                                            ? 'opacity-75'
                                            : 'cursor-pointer hover:border-primary/50 hover:bg-primary/5 active:scale-95'
                                }`}
                                onClick={() => tradeable && handleStockSelect(id)}
                            >
                                <div className="flex items-start justify-between">
                                    <div className="space-y-2 flex-1">
                                        <div className="flex items-center gap-2">
                                            <span className="font-medium">{ticker}</span>
                                            <Badge
                                                variant={change >= 0 ? "default" : "destructive"}
                                                className="text-xs"
                                            >
                                                {change >= 0 ? '+' : ''}{changePercent.toFixed(2)}%
                                            </Badge>
                                            {isSelected && (
                                                <div className="flex items-center gap-1">
                                                    <Check className="w-4 h-4 text-green-600"/>
                                                    <span className="text-xs text-green-600 font-medium">Selected</span>
                                                </div>
                                            )}
                                        </div>
                                        <p className="text-sm text-muted-foreground">{name}</p>
                                        <p className="text-xs text-blue-600 italic">{reasonForSelection}</p>
                                    </div>
                                    <div className="text-right">
                                        <div className="font-medium">${previousClose.toFixed(2)}</div>
                                        <div className={`text-sm ${change >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                                            {change >= 0 ? '+' : ''}{change.toFixed(2)}
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        );
                    })}

                    {/* Skip option */}
                    <Button
                        variant="outline"
                        className={`w-full h-auto p-4 transition-all duration-200 ${
                            selectedStock && userPick.pickId === null
                                ? 'border-green-500 bg-green-50 shadow-md'
                                : (!isAuthenticated || !tradeable)
                                    ? 'opacity-75'
                                    : 'hover:bg-primary/5 active:scale-95'
                        }`}
                        onClick={() => tradeable && onRemovePick(tradeDate)
                    }
                    >
                        <div className="text-center flex items-center justify-center gap-2 w-full">
                            <div>
                                <div className="font-medium">Skip Today</div>
                                <div className="text-sm text-muted-foreground">
                                    Don't invest today
                                </div>
                            </div>
                            {                            userPick !== null && userPick.pickId === null
                                && (
                                <div className="flex items-center gap-1">
                                    <Check className="w-4 h-4 text-green-600"/>
                                    <span className="text-xs text-green-600 font-medium">Selected</span>
                                </div>
                            )}
                        </div>
                    </Button>
                </div>

                {/* Footer info */}
                <div className="text-center text-xs text-muted-foreground space-y-1">
                    <p>Balance resets to $100 at the start of each month</p>
                    <p>P&L calculated after market close</p>
                </div>
            </div>
        </div>
    );}