import React from 'react';
interface HundredBucksLogoProps {
  className?: string;
  showText?: boolean;
}

export function HundredBucksLogo({ className = "", showText = true }: HundredBucksLogoProps) {
  return (
    <div className={`flex items-center gap-2 ${className}`}>
      {/* Logo Icon */}
      {/*<div className="relative">*/}
      {/*  <svg */}
      {/*    width="32" */}
      {/*    height="32" */}
      {/*    viewBox="0 0 32 32" */}
      {/*    fill="none" */}
      {/*    className="text-primary"*/}
      {/*  >*/}
      {/*    /!* Circle background *!/*/}
      {/*    <circle */}
      {/*      cx="16" */}
      {/*      cy="16" */}
      {/*      r="15" */}
      {/*      fill="currentColor" */}
      {/*      stroke="currentColor" */}
      {/*      strokeWidth="1"*/}
      {/*    />*/}
      {/*    */}
      {/*    /!* Dollar sign *!/*/}
      {/*    <path */}
      {/*      d="M12 10h8M12 22h8M16 8v2M16 22v2M14 14h6c1.1 0 2 .9 2 2s-.9 2-2 2h-2M12 18h6c1.1 0 2-.9 2-2s-.9-2-2-2h-2" */}
      {/*      stroke="white" */}
      {/*      strokeWidth="2" */}
      {/*      strokeLinecap="round" */}
      {/*      strokeLinejoin="round"*/}
      {/*      fill="none"*/}
      {/*    />*/}
      {/*    */}
      {/*    /!* Small "100" *!/*/}
      {/*    <text */}
      {/*      x="24" */}
      {/*      y="12" */}
      {/*      fontSize="8" */}
      {/*      fill="currentColor" */}
      {/*      fontWeight="bold"*/}
      {/*      className="text-primary"*/}
      {/*    >*/}
      {/*      100*/}
      {/*    </text>*/}
      {/*  </svg>*/}
      {/*</div>*/}
      
      {/* Text logo */}
      {showText && (
        <div className="flex flex-col leading-none">
          <span className="font-bold text-lg text-primary">#HundredBucks</span>
        </div>
      )}
    </div>
  );
}