import React, {useEffect, useState} from 'react';
import {Card} from './ui/card';
import {Button} from './ui/button';
import {ArrowLeft, Award, Medal, Trophy} from 'lucide-react';
import {HundredBucksLogo} from './HundredBucksLogo';
import {LeaderBoard} from "../models";
import {getLeaderBoard} from "../api";

interface LeaderboardProps {
  onBack: () => void;
}

export function Leaderboard({ onBack }: LeaderboardProps) {
  const [leaderBoard, setLeaderBoard] = useState<LeaderBoard | null>(null);

  useEffect(() => {
    getLeaderBoard().then(async leaderBoard => {
      setLeaderBoard(leaderBoard)
    });
  }, [])

  const getRankIcon = (rank: number) => {
    switch (rank) {
      case 1:
        return <Trophy className="w-5 h-5 text-yellow-500" />;
      case 2:
        return <Medal className="w-5 h-5 text-gray-400" />;
      case 3:
        return <Award className="w-5 h-5 text-amber-600" />;
      default:
        return <span className="w-5 h-5 flex items-center justify-center text-sm font-medium text-muted-foreground">#{rank}</span>;
    }
  };

  const currentMonth = new Date().toLocaleDateString('en-US', { 
    month: 'long', 
    year: 'numeric' 
  });

  return (
    <div className="min-h-screen bg-background p-4">
      <div className="w-full max-w-2xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={onBack} className="p-2">
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div className="flex-1 text-center">
            <HundredBucksLogo className="justify-center mb-2" />
            <p className="text-sm text-muted-foreground">{currentMonth} Leaderboard</p>
          </div>
          <div className="w-10"></div> {/* Spacer for centering */}
        </div>

        <div className="space-y-3">
          <h2 className="text-lg font-medium text-center">Top 10</h2>
          {(leaderBoard?.top10 || []).map((player, index) => (
            <Card 
              key={player.userName}
              className={`p-4 ${player.userName === 'You' ? 'border-primary/50 bg-primary/5' : ''}`}
            >
              <div className="flex items-center gap-4">
                <div className="flex-shrink-0">
                  {getRankIcon(index + 1)}
                </div>

                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <span className="font-medium truncate">
                      {player.userName}
                    </span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-muted-foreground">
                    <span>Last: {player.lastTradedTicker ?? "None"}</span>
                  </div>
                </div>

                <div className="text-right space-y-1">
                  <div className="font-medium">${player.currentBalance.toFixed(2)}</div>
                  <div className={`text-sm ${player.lastIncrement >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                    {player.lastIncrement >= 0 ? '+' : ''}${player.lastIncrement.toFixed(2)}
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>

        {/* Footer info */}
        <div className="text-center text-xs text-muted-foreground space-y-1">
          <p>Rankings update after market close each day</p>
          <p>Balances reset to $100 at the start of each month</p>
        </div>
      </div>
    </div>
  );
}