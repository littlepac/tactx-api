import React, { useState } from 'react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Input } from './ui/input';
import { Label } from './ui/label';
import { ArrowLeft, User, LogOut } from 'lucide-react';
import { HundredBucksLogo } from './HundredBucksLogo';

interface UserSettingsProps {
  onBack: () => void;
  onLogout: () => void;
  currentUsername: string;
  onUsernameChange: (newUsername: string) => void;
}

export function UserSettings({ onBack, onLogout, currentUsername, onUsernameChange }: UserSettingsProps) {
  const [newUsername, setNewUsername] = useState(currentUsername);
  const [isEditing, setIsEditing] = useState(false);

  const handleSaveUsername = () => {
    if (newUsername.trim() && newUsername !== currentUsername) {
      onUsernameChange(newUsername.trim());
    }
    setIsEditing(false);
  };

  const handleCancelEdit = () => {
    setNewUsername(currentUsername);
    setIsEditing(false);
  };

  return (
    <div className="min-h-screen bg-background p-4">
      <div className="w-full max-w-md mx-auto space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={onBack} className="p-2">
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div className="flex-1 text-center">
            <HundredBucksLogo className="justify-center mb-2" />
            <p className="text-sm text-muted-foreground">Settings</p>
          </div>
          <div className="w-10"></div> {/* Spacer for centering */}
        </div>

        {/* User Profile Section */}
        <Card className="p-6 space-y-4">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center">
              <User className="w-6 h-6 text-primary" />
            </div>
            <div>
              <p className="font-medium">Welcome back!</p>
              <p className="text-sm text-muted-foreground">Manage your account</p>
            </div>
          </div>
        </Card>

        {/* Username Section */}
        <Card className="p-6 space-y-4">
          <div className="space-y-3">
            <Label htmlFor="username">Username</Label>
            {isEditing ? (
              <div className="space-y-3">
                <Input
                  id="username"
                  value={newUsername}
                  onChange={(e) => setNewUsername(e.target.value)}
                  placeholder="Enter username"
                  maxLength={20}
                />
                <div className="flex gap-2">
                  <Button onClick={handleSaveUsername} size="sm" className="flex-1">
                    Save
                  </Button>
                  <Button onClick={handleCancelEdit} variant="outline" size="sm" className="flex-1">
                    Cancel
                  </Button>
                </div>
              </div>
            ) : (
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <p className="font-medium">{currentUsername}</p>
                  <p className="text-sm text-muted-foreground">Your display name</p>
                </div>
                <Button onClick={() => setIsEditing(true)} variant="outline" size="sm">
                  Edit
                </Button>
              </div>
            )}
          </div>
        </Card>

        {/* Account Actions */}
        <Card className="p-6 space-y-4">
          <div className="space-y-3">
            <h3 className="font-medium">Account</h3>
            <Button 
              onClick={onLogout} 
              variant="destructive" 
              className="w-full justify-start gap-2"
            >
              <LogOut className="w-4 h-4" />
              Sign Out
            </Button>
          </div>
        </Card>

        {/* App Info */}
        <div className="text-center text-xs text-muted-foreground space-y-1">
          <p>Hundred Bucks v1.0</p>
          <p>Your daily stock trading game</p>
        </div>
      </div>
    </div>
  );
}