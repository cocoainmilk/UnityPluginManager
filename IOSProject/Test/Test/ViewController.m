//
//  ViewController.m
//  Test
//
//  Created by nano on 2020/08/07.
//  Copyright © 2020 NanoCompany. All rights reserved.
//

#import "ViewController.h"
#import <UnityPluginBridge/UnityPluginBridge.h>


@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    RequestBridge(0, "apple", "login", nil, self);
}


@end
