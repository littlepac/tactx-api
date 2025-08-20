import {Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle} from './ui/dialog';
import React from 'react';
import {Check} from 'lucide-react';

interface StockSelectionModalProps {
    isOpen: boolean;
    onClose: () => void;
    comment?: string | null;
}

export function StockSelectionModal({
                                        isOpen,
                                        onClose,
                                        comment
                                    }: StockSelectionModalProps) {
    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-md">
                <DialogHeader className="text-center">
                    <div className="mx-auto w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mb-4">
                        <Check className="w-6 h-6 text-green-600"/>
                    </div>
                    <DialogTitle>Congratulations on your pick!</DialogTitle>
                    {comment && <DialogDescription className="text-center">
                        {comment}
                    </DialogDescription>
                    }
                </DialogHeader>
            </DialogContent>
        </Dialog>
    );
}