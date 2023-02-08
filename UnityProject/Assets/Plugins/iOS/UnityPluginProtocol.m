//
//  BridgeObjectiveC.m
//  UnityPluginCore
//
//  Created by  on 2020/01/04.
//  Copyright © 2020 Company. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UnityPluginProtocol.h"
#import "UnityInterface.h"

@implementation UnityPluginProtocol
- (UIViewController* _Nonnull)getUnityViewController
{
    return UnityGetGLViewController();
}
- (void)onComletedWithMessage:(NSString * _Nonnull)message
{
    NSLog(@"onCompleted");
    UnitySendMessage("PluginManager", "OnCompleted", [message UTF8String]);
}
@end
