import React from 'react';
import {Card} from './ui/card';
import {HundredBucksLogo} from './HundredBucksLogo';
import {CredentialResponse, GoogleLogin} from "@react-oauth/google";

interface LoginProps {
  onGoogleLoginSuccess: (credential: string) => void;
}

export function Login({ onGoogleLoginSuccess }: LoginProps) {
  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="w-full max-w-md mx-auto space-y-6">
        {/* Logo and Welcome */}
        <div className="text-center space-y-4">
          <HundredBucksLogo className="justify-center" />
          <div className="space-y-2">
            <h1 className="text-2xl font-medium">Welcome to Hundred Bucks</h1>
            <p className="text-muted-foreground">
              Start with $100 each month. Pick stocks daily and climb the leaderboard!
            </p>
          </div>
        </div>

        {/* Login Card */}
        <Card className="p-6 space-y-6">
          <div className="text-center space-y-2">
            <h2 className="text-lg font-medium">Sign in to play</h2>
            <p className="text-sm text-muted-foreground">
              Join other players in the ultimate stock picking challenge
            </p>
          </div>

          <GoogleLogin onSuccess={(response : CredentialResponse) => onGoogleLoginSuccess(response.credential!!)}/>
          <div className="space-y-3 pt-4 border-t">
            <div className="flex items-center gap-3 text-sm">
              <div className="w-2 h-2 bg-green-500 rounded-full"></div>
              <span>Pick from 4 curated stocks daily</span>
            </div>
            <div className="flex items-center gap-3 text-sm">
              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
              <span>Track your performance vs others</span>
            </div>
            <div className="flex items-center gap-3 text-sm">
              <div className="w-2 h-2 bg-purple-500 rounded-full"></div>
              <span>Reset to $100 every month</span>
            </div>
          </div>
        </Card>

        {/* Footer */}
        <div className="text-center text-xs text-muted-foreground space-y-1">
          <p>By signing in, you agree to our terms of service</p>
          <p>Free to play • No real money involved</p>
        </div>
      </div>
    </div>
  );
}